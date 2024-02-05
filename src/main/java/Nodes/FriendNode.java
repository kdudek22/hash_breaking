package Nodes;

import io.libp2p.core.PeerId;

public class FriendNode {
    public PeerId peerId;
    public Friend friend;

    public FriendNode(PeerId peerId, Friend friend){
        this.peerId = peerId;
        this.friend = friend;
    }

    public void sendMessage(String message){
        this.friend.chatController.send(message);
    }
}
