package command;

import io.libp2p.core.PeerId;

public class SolvedCommand implements Command{
    String message;
    PeerId id;
    public SolvedCommand(String message, PeerId id){
        this.message = message;
        this.id = id;
    }

    @Override
    public void execute() {
        System.out.println(id.toBase58() + " " + this.message);
        StopAndCleanCommand command = new StopAndCleanCommand();
        command.execute();
    }
}
