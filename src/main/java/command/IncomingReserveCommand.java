package command;

import Nodes.Node;
import Nodes.StringInterval;
import io.libp2p.core.PeerId;

public class IncomingReserveCommand implements Command{
    public String message;
    public PeerId id;
    public IncomingReserveCommand(String message, PeerId id){
        this.message = message;
        this.id = id;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();

        String both = message.split("-")[1];
        String firstString = both.split(":")[0];
        String secondString = both.split(":")[1];
        StringInterval receivedInterval = new StringInterval(firstString,secondString);
        node.recentReserves.put(this.id, receivedInterval);
        node.checkIfReserveDoesNotCollide(receivedInterval, this.id);
    }
}
