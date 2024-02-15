package subscriberAndPublisher;
import io.libp2p.core.PeerId;

import java.util.ArrayList;
import java.util.List;

public class NodePublisher implements Publisher{

    public static NodePublisher intance;
    List<FriendNode> subscribers = new ArrayList<>();

    public static synchronized NodePublisher getInstance(){
        if(intance==null){
            intance = new NodePublisher();
        }
        return intance;
    }

    public boolean peerAlreadyInSubscribed(PeerId peerId){
        return this.getByPeerId(peerId) != null;
    }

    public void sendMessageToSingleSubscriber(String message, PeerId peerId){
        FriendNode friendNode = getByPeerId(peerId);
        if(friendNode == null){
            System.out.println("COULD NOT FIND NODE");
            return;
        }

        friendNode.sendMessage(message);
    }

    public FriendNode getByPeerId(PeerId peerId){
        for(var node: subscribers){
            if(node.peerId.toBase58().equals(peerId.toBase58())){
                return node;
            }
        }
        return null;
    }

    @Override
    public void sendMessageToSubscribers(String message) {
        for(var subscriber: this.subscribers){
            subscriber.sendMessage(message);
        }
    }

    @Override
    public synchronized void addSubscriber(FriendNode friendNode) {
        if(this.subscribers.stream().anyMatch(node -> node.peerId.toBase58().equals(friendNode.peerId.toBase58()))){
            return;
        }
        System.out.println("NODE " + friendNode.peerId + " HAS CONNECTED");
        this.subscribers.add(friendNode);
    }

    @Override
    public synchronized void removeSubscriber(FriendNode friendNode) {
        if(this.subscribers.stream().noneMatch(node -> node.peerId.toBase58().equals(friendNode.peerId.toBase58()))){
            return;
        }
        System.out.println("NODE " + friendNode.peerId + " HAS DISCONNECTED");
        this.subscribers.remove(friendNode);
    }

}
