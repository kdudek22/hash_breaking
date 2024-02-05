package subscriberAndPublisher;
import Nodes.FriendNode;

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
    @Override
    public void sendMessageToSubscribers(String message) {
        for(var x: this.subscribers){
            x.sendMessage(message);
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
