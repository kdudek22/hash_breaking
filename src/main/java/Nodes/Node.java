package Nodes;
import command.*;
import hashBreaker.StringInterval;
import hashBreaker.StringProvider;
import io.libp2p.core.*;
import io.libp2p.core.dsl.HostBuilder;
import io.libp2p.discovery.MDnsDiscovery;
import io.libp2p.example.chat.Chat;
import io.libp2p.example.chat.ChatController;
import kotlin.Pair;
import protocol.FriendNodeChatController;
import subscriberAndPublisher.FriendNode;
import subscriberAndPublisher.NodePublisher;
import utils.SleepUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Node {

    public static Node instance;
    public Host host;
    public InetAddress address;
    public String addressString;
    public Discoverer discoverer;
    public int solveBatchAmount = 3000000;
    public List<StringInterval> alreadyDone = Collections.synchronizedList(new ArrayList<>());
    public String hashToFind;
    public boolean startNextInterval = true;
    public boolean finished = true;
    public String currentStartString;
    public String currentEndString;
    public boolean conflict;
    public StringInterval possibleInterval;

    public Map<PeerId, StringInterval> recentReserves = new ConcurrentHashMap<>();

    public Map<PeerId, List<StringInterval>> jobs = new ConcurrentHashMap<>();

    public static boolean showOutput = false;

    public static Node getInstance(){
        if(instance == null){
            instance = new Node();
        }
        return instance;
    }

    public Node(){
        try{
            this.address = getPrivateIp();
            this.addressString = this.address.toString().substring(1);

            this.host = new HostBuilder().protocol(new Chat(this::messageReceived)).listen("/ip4/"+this.addressString+"/tcp/0").build();
            this.host.start().get();
            this.discoverer = new MDnsDiscovery(this.host,"_ipfs-discovery._udp.local.",120,this.address);
            this.discoverer.getNewPeerFoundListeners().add((peer)->{
                this.peerFound(peer);
                return null;
            });
            this.discoverer.start();

//            this.hashToFind = StringProvider.getHashFromString("asdgas");
//            this.startHashBreaker();
        }
        catch (Exception e){
            System.out.println("FAILED TO CREATE");
        }
    }

    public void callbackFromHashBreaker(String foundString){
        if(foundString.equals("BOUNDARY")){
            ReachedBoundaryCommand command = new ReachedBoundaryCommand();
            command.execute();
        }
        else if(!foundString.equals("")){
            FoundSolutionCommand command = new FoundSolutionCommand(foundString);
            command.execute();
        }
        else{
            SearchedIntervalCommand command = new SearchedIntervalCommand();
            command.execute();
        }
    }

    public void startHashBreaker(){
        this.finished = false;
        new Thread(()->{
            while(this.shouldRunHashBreaker()){
                if(this.shouldStartNextInterval()){

                    this.checkIfConflictsOccurAndResolve();

                    if(this.shouldSolveInterval()){
                        SolveHashIntervalCommand command = new SolveHashIntervalCommand(this.hashToFind);
                        command.execute();
                    }
                }
                else{
                    SleepUtils.sleepTimeAmount(3000);
                }
            }
            System.out.println("FINISHED");
        }).start();
    }


    public void checkIfConflictsOccurAndResolve(){
        boolean firstTime = true;
        while(this.conflict || firstTime){
            this.conflict = false;
            this.reserveStringInterval();

            this.checkIfRecentReserverDoNotCollide();

            SleepUtils.sleepTimeAmount(1500);

            this.checkIfReserveDoesNotCollideWithAlreadyDone();
            firstTime = false;
        }
    }
    public void checkIfRecentReserverDoNotCollide(){
        for(var x : this.recentReserves.keySet()){
            this.checkIfReserveDoesNotCollide(this.recentReserves.get(x),x);
        }
    }

    public void reserveStringInterval(){
        BroadcastReserveCommand reserveCommand = new BroadcastReserveCommand();
        reserveCommand.execute();
    }

    public String calcluateStartString(){
        if(this.alreadyDone.isEmpty() || !this.alreadyDone.get(0).startString.equals("a")){
            return "a";
        }
        for(int i=0;i<this.alreadyDone.size()-1;i++){
            if(!StringProvider.generateNextString(this.alreadyDone.get(i).endString).equals(this.alreadyDone.get(i+1).startString) && !this.alreadyDone.get(i).equals(this.alreadyDone.get(i+1))){
                return StringProvider.generateNextString(this.alreadyDone.get(i).endString);
            }
        }
        String lastString = this.alreadyDone.get(this.alreadyDone.size()-1).endString;
        return StringProvider.generateNextString(lastString);
    }

    public String calculateEndString(String startString){
        return StringProvider.convertNumberToString(this.solveBatchAmount+StringProvider.convertStringToNumber(startString));
    }

    public void checkIfReserveDoesNotCollide(StringInterval stringInterval, PeerId peerId){
        if(this.possibleInterval.equals(stringInterval)){
            if(this.host.getPeerId().toBase58().compareTo(peerId.toBase58())<0){
                NodePublisher nodePublisher = NodePublisher.getInstance();
                nodePublisher.sendMessageToSingleSubscriber("CONFLICT", peerId);
            }
            else{
                this.conflict  = true;
            }
        }
    }

    public void checkIfReserveDoesNotCollideWithAlreadyDone(){
        for(StringInterval doneInterval: this.alreadyDone){
            if(doneInterval.equals(this.possibleInterval)){
                this.conflict = true;
                return;
            }
        }
    }

    public void broadcastInterval(){
        NodePublisher nodePublisher = NodePublisher.getInstance();
        nodePublisher.sendMessageToSubscribers("SOLVING-"+this.currentStartString+":"+this.currentEndString);
    }

    public void broadcastPossibleInterval(String startString, String endString){
        NodePublisher nodePublisher = NodePublisher.getInstance();
        nodePublisher.sendMessageToSubscribers("RESERVE-"+startString+":"+endString);
    }

    public void peerFound(PeerInfo info) {
        NodePublisher publisher = NodePublisher.getInstance();
        if (this.peerIsAlreadyAdded(info)) {
            return;
        }

        var chatConnection = connectChat(info);
        FriendNode friendNode = new FriendNode(info.getPeerId(), new FriendNodeChatController(info.getPeerId().toBase58(),chatConnection.getSecond()));
        publisher.addSubscriber(friendNode);

        if(!this.jobs.containsKey(info.getPeerId())){
            this.jobs.put(info.getPeerId(), new ArrayList<>());
        }

        if(this.hasHashToFind()){
            publisher.sendMessageToSingleSubscriber("SOLVE THIS " + this.hashToFind, info.getPeerId());
            if(this.hasIntervalToSolve()){
                publisher.sendMessageToSingleSubscriber("RESERVE-"+currentStartString+":"+currentEndString,info.getPeerId());
                this.broadcastInterval();
                publisher.sendMessageToSingleSubscriber("DONE:"+this.alreadyDone.toString(), info.getPeerId());
            }
        }

        chatConnection.getFirst().closeFuture().thenAccept((e)->{
            handleDisconnect(friendNode);

        });
    }

    public boolean shouldSolveInterval(){
        return !this.finished && this.hashToFind!=null;
    }
    public boolean shouldStartNextInterval(){
        return this.startNextInterval && !this.finished && this.hashToFind!=null;
    }
    public boolean shouldRunHashBreaker(){
        return !this.finished && this.hashToFind!=null;
    }

    public boolean hasHashToFind(){
        return this.hashToFind != null;
    }

    public boolean hasIntervalToSolve(){
        return this.currentEndString != null;
    }
    public boolean peerIsAlreadyAdded(PeerInfo info){
        NodePublisher publisher = NodePublisher.getInstance();
        return info.getPeerId().equals(this.host.getPeerId()) || publisher.peerAlreadyInSubscribed(info.getPeerId());
    }

    public void handleDisconnect(FriendNode friendNode){
        NodePublisher publisher = NodePublisher.getInstance();
        publisher.removeSubscriber(friendNode);

        if(!this.finished){
            this.handleDisconnectedNodeLastJob(friendNode);

        }
    }
    public void handleDisconnectedNodeLastJob(FriendNode friendNode){
        if(this.jobs.containsKey(friendNode.peerId)) {
            DisconectedNodeJobCommand command = new DisconectedNodeJobCommand(friendNode);
            command.execute();
        }
    }

    public String messageReceived(PeerId id, String message){
        if(showOutput){
            System.out.println("MESSAGE FROM " + id.toBase58() + ": " + message);
        }

        if(message.startsWith("DONE")){
            AlreadyDoneCommand command = new AlreadyDoneCommand(message);
            command.execute();
        }

        else if(message.startsWith("SOLVED")){
            SolvedCommand command = new SolvedCommand(message, id);
            command.execute();
        }

        else if(message.startsWith("SOLVE")){
            SolveCommand solveCommand = new SolveCommand(message);
            solveCommand.execute();
        }

        else if(message.startsWith("RESERVE")){
            IncomingReserveCommand command = new IncomingReserveCommand(message, id);
            command.execute();
        }

        else if(message.startsWith("SOLVING")){
            IncomingSolvingCommand command = new IncomingSolvingCommand(message, id);
            command.execute();
        }

        return "";
    }


    public InetAddress getPrivateIp() {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            System.out.println("THE IP IS: "+ip);
            return InetAddress.getByName(ip);
        }
        catch (Exception e){
            return null;
        }
    }

    public Pair<Stream, ChatController> connectChat(PeerInfo info) {
        try {
            var chat = new Chat(this::messageReceived).dial(this.host,info.getPeerId(), info.getAddresses().get(0));
            return new Pair(chat.getStream().get(),chat.getController().get());
        }
        catch (Exception e){
            return null;
        }
    }

    public void cleanVariables(){
        this.finished = true;
        this.recentReserves = new ConcurrentHashMap<>();
        this.jobs = new ConcurrentHashMap<>();
        this.hashToFind = null;
        this.startNextInterval = true;
        this.currentStartString = null;
        this.currentEndString = null;
        this.conflict = false;
        this.possibleInterval = null;
        this.alreadyDone = Collections.synchronizedList(new ArrayList<>());
        System.out.println("CLEANED NODE");
    }

}
