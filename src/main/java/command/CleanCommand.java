package command;

import node.Node;

public class CleanCommand implements Command{
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.cleanVariables();
    }
}
