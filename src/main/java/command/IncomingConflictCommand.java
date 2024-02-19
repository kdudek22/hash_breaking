package command;

import node.Node;

public class IncomingConflictCommand implements Command{
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.conflict = true;
        if(!node.alreadyDone.contains(node.possibleInterval)){
            node.alreadyDone.add(node.possibleInterval);
        }
    }
}
