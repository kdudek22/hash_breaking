package command;

import node.Node;

public class ReachedBoundaryCommand implements Command{
    @Override
    public void execute() {
        if(Node.showOutput) {
            System.out.println("??? REACHED THE BOUNDARY ???");
        }
        StopAndCleanCommand command = new StopAndCleanCommand();
        command.execute();
    }
}
