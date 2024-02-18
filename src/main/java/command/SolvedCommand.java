package command;

public class SolvedCommand implements Command{
    String message;
    public SolvedCommand(String message){
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(this.message);
        StopAndCleanCommand command = new StopAndCleanCommand();
        command.execute();
    }
}
