package command;

import Nodes.Node;
import hashBreaker.StringInterval;
import io.libp2p.core.PeerId;

import java.util.Collections;

public class IncomingSolvingCommand implements Command{
    public String message;
    public PeerId id;
    public IncomingSolvingCommand(String message, PeerId id){
        this.message = message;
        this.id = id;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        String both = message.split("-")[1];
        String firstString = both.split(":")[0];
        String secondString = both.split(":")[1];



        StringInterval stringInterval = new StringInterval(firstString, secondString);
        node.jobs.get(id).add(stringInterval);
        node.alreadyDone.add(stringInterval);
        Collections.sort(node.alreadyDone);
    }
}
