package protocol;
import io.libp2p.example.chat.ChatController;

public class FriendNodeChatController {
    public String name;
    public ChatController chatController;

    public FriendNodeChatController(String name, ChatController controller){
        this.name = name;
        this.chatController = controller;
    }
}