package command;

import Nodes.Node;

public class CleanCommand implements Command{
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.cleanVariables();
    }
}
