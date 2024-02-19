package subscriberAndPublisher;

public interface Publisher {
    public void sendMessageToSubscribers(String message);
    public void addSubscriber(Subscriber subscriber);
    public void removeSubscriber(Subscriber subscriber);
}
