package subscriberAndPublisher;

import io.libp2p.core.PeerId;
import protocol.FriendNodeChatController;

public class FriendNode implements Subscriber{
    public PeerId peerId;
    public FriendNodeChatController friend;

    public FriendNode(PeerId peerId, FriendNodeChatController friend){
        this.peerId = peerId;
        this.friend = friend;
    }

    @Override
    public void sendMessage(String message){
        this.friend.chatController.send(message);
    }
}
