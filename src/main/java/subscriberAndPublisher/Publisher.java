package subscriberAndPublisher;

import Nodes.FriendNode;

public interface Publisher {
    public void sendMessageToSubscribers(String message);
    public void addSubscriber(FriendNode friendNode);
    public void removeSubscriber(FriendNode friendNode);
}
