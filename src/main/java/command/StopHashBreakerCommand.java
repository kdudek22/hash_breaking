package command;

import Nodes.Node;
import hashBreaker.HashBreaker;

public class StopHashBreakerCommand implements Command{
    @Override
    public void execute() {
        HashBreaker h = HashBreaker.getInstance();
        Node n = Node.getInstance();
        h.stopBreaker();
        n.finished = true;
    }
}
