package command;

import Nodes.Node;

public class SolveCommand implements Command{
    String hashToFind;
    public SolveCommand(String solveCommand){
        this.hashToFind = solveCommand.split(" ")[2].strip();
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.hashToFind = this.hashToFind;
    }
}
