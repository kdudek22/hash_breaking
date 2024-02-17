package command;

import Nodes.Node;
import hashBreaker.StringInterval;

import java.util.Collections;

public class SearchedIntervalCommand implements Command{
    @Override
    public void execute() {
        Node node = Node.getInstance();
        StringInterval searchedInterval = new StringInterval(node.currentStartString,node.currentEndString);
        if(Node.showOutput) {
            System.out.println("*** SEARCHED INTERVAL " + searchedInterval + " ***");
        }
        if(node.currentStartString!=null){
            if(!node.alreadyDone.contains(searchedInterval)){
                node.alreadyDone.add(searchedInterval);
            }
            Collections.sort(node.alreadyDone);
            node.startNextInterval = true;
        }
    }
}
