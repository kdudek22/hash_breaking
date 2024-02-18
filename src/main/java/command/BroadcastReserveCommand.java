package command;

import Nodes.Node;
import hashBreaker.StringInterval;
import subscriberAndPublisher.NodePublisher;

public class BroadcastReserveCommand implements Command{

    @Override
    public void execute() {
        Node node = Node.getInstance();
        NodePublisher nodePublisher = NodePublisher.getInstance();

        String possibleStartString = node.calcluateStartString();
        StringInterval possibleInterval = new StringInterval(possibleStartString, node.calculateEndString(possibleStartString));

        node.possibleInterval = possibleInterval;
        node.conflict=false;
        if(Node.showOutput) {
            System.out.println("RESERVING " + node.possibleInterval);
        }

        nodePublisher.sendMessageToSubscribers("RESERVE-"+node.possibleInterval.startString+":"+node.possibleInterval.endString);
    }
}
