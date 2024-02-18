package command;

import node.Node;
import hashBreaker.StringInterval;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncomingAlreadyDoneCommand implements Command{
    public String message;
    public IncomingAlreadyDoneCommand(String message){
        this.message = message;
    }
    @Override
    public void execute() {
        Node node = Node.getInstance();
        String intervals = message.split(":")[1];
        intervals = intervals.substring(1, intervals.length()-1);
        String[] splitted = intervals.split(",");
        List<StringInterval> newIntervals = new ArrayList<>();
        for(int i=0; i<splitted.length; i++){
            newIntervals.add(new StringInterval(splitted[i].strip()));
        }
        if(node.alreadyDone.isEmpty()){
            node.alreadyDone = newIntervals;
            return;
        }
        for(var i : newIntervals){
            if(!node.alreadyDone.contains(i)){
                node.alreadyDone.add(i);
                Collections.sort(node.alreadyDone);
            }
        }


    }
}
