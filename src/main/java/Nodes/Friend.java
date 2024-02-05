package Nodes;
import io.libp2p.example.chat.ChatController;

public class Friend {
    public String name;

    public ChatController chatController;

    public Friend(String name, ChatController controller){
        this.name = name;
        this.chatController = controller;
    }
}