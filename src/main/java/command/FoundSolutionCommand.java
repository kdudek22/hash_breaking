package command;

import Nodes.Node;
import hashBreaker.StringProvider;
import subscriberAndPublisher.NodePublisher;

public class FoundSolutionCommand implements Command{
    public String resString;
    public FoundSolutionCommand(String resString){
        this.resString = resString;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        System.out.println("FOUND SOLUTION " + resString + " " + StringProvider.getHashFromString(resString) + " " + node.hashToFind);
        NodePublisher publisher = NodePublisher.getInstance();
        publisher.sendMessageToSubscribers("SOLVED:"+this.resString     );
        StopAndCleanCommand command = new StopAndCleanCommand();
        command.execute();
    }
}
