package command;

import Nodes.Node;
import hashBreaker.HashBreaker;

public class SolveHashIntervalCommand implements Command{
    String startString;
    String endString;
    String hastToFind;

    public SolveHashIntervalCommand(String startString, String endString, String hashToFind){
        this.startString = startString;
        this.endString = endString;
        this.hastToFind = hashToFind;
    }

    @Override
    public void execute() {
        Node n = Node.getInstance();
        n.broadcastInterval();
        HashBreaker h = HashBreaker.getInstance();
        h.startBreaker(this.startString, this.endString, this.hastToFind);
    }
}
