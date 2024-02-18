package command;

import node.Node;
import hashBreaker.StringInterval;
import subscriberAndPublisher.FriendNode;

public class DisconectedNodeJobCommand implements Command{
    public FriendNode friendNode;
    public DisconectedNodeJobCommand(FriendNode friendNode){
        this.friendNode = friendNode;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        
        StringInterval interval = null;
        for(var alreadyDone: node.alreadyDone){
            if(node.jobs.get(friendNode.peerId).get(node.jobs.size()-1).equals(alreadyDone)){
                interval = alreadyDone;
            }
        }
        node.alreadyDone.remove(interval);
        node.recentReserves.remove(friendNode.peerId);
        if(Node.showOutput) {
            System.out.println("THEIR LAST JOB WAS " + node.jobs.get(friendNode.peerId).get(node.jobs.size() - 1));
        }

    }


}
