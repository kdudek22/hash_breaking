package command;

import Nodes.Node;
import hashBreaker.HashBreaker;

public class SolveHashIntervalCommand implements Command{
    String hastToFind;

    public SolveHashIntervalCommand(String hashToFind){
        this.hastToFind = hashToFind;
    }

    @Override
    public void execute() {
        Node n = Node.getInstance();
        n.startNextInterval = false;
        n.currentStartString = n.calcluateStartString();
        n.currentEndString = n.calculateEndString(n.currentStartString);

        n.broadcastInterval();
        HashBreaker h = HashBreaker.getInstance();
        h.startBreaker(n.currentStartString, n.currentEndString, this.hastToFind);
    }
}
