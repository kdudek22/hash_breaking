package command;

import subscriberAndPublisher.NodePublisher;

public class FoundSolutionCommand implements Command{
    public String resString;
    public FoundSolutionCommand(String resString){
        this.resString = resString;
    }
    @Override
    public void execute() {
        NodePublisher publisher = NodePublisher.getInstance();
        publisher.sendMessageToSubscribers("SOLVED:"+this.resString     );
        StopAndCleanCommand command = new StopAndCleanCommand();
        command.execute();
    }
}
