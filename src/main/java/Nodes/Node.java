package Nodes;
import io.libp2p.core.*;
import io.libp2p.core.dsl.HostBuilder;
import io.libp2p.discovery.MDnsDiscovery;
import io.libp2p.example.chat.Chat;
import io.libp2p.example.chat.ChatController;
import kotlin.Pair;
import subscriberAndPublisher.NodePublisher;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public Host host;
    public InetAddress address;
    public String addressString;
    public Discoverer discoverer;
    public List<PeerId> knownNodes = new ArrayList<>();

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
        }
        catch (Exception e){
            System.out.println("FAILED TO CREATE");
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

    public void send(String message) {
        NodePublisher p = NodePublisher.getInstance();
        p.sendMessageToSubscribers(message);
    }

    public void peerFound(PeerInfo info) {
        if (info.getPeerId().equals(this.host.getPeerId()) || this.knownNodes.contains(info.getPeerId())) {
            return;
        }

        NodePublisher publisher = NodePublisher.getInstance();
        var chatConnection = connectChat(info);
        FriendNode friendNode = new FriendNode(info.getPeerId(), new Friend(info.getPeerId().toBase58(),chatConnection.getSecond()));
        publisher.addSubscriber(friendNode);


        chatConnection.getFirst().closeFuture().thenAccept((e)->{
            handleDisconnect(friendNode);

        });
    }
    public void handleDisconnect(FriendNode friendNode){
        NodePublisher publisher = NodePublisher.getInstance();
        publisher.removeSubscriber(friendNode);
    }

    public String messageReceived(PeerId id, String message){
        System.out.println("MESSAGE FROM " + id.toBase58() + ": " + message);
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
}
