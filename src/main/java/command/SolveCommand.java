package command;

import node.Node;

public class SolveCommand implements Command{
    String hashToFind;
    public SolveCommand(String solveCommand){
        this.hashToFind = solveCommand.split(" ")[2].strip();
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.hashToFind = this.hashToFind;
        synchronized (node){
            if(node.finished){
                node.finished = false;
                node.startHashBreaker();
            }
        }
    }
}
