package Nodes;

import io.libp2p.core.PeerId;
import protocol.FriendNodeChatController;

public class FriendNode {
    public PeerId peerId;
    public FriendNodeChatController friend;

    public FriendNode(PeerId peerId, FriendNodeChatController friend){
        this.peerId = peerId;
        this.friend = friend;
    }

    public void sendMessage(String message){
        this.friend.chatController.send(message);
    }
}
