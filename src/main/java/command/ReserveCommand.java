package command;

import Nodes.Node;

public class ReserveCommand implements Command{
    public String startString;
    public String endString;
    public ReserveCommand(String startString, String endString){
        this.startString = startString;
        this.endString = endString;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        node.broadcastPossibleInterval(startString, endString);
    }
}
