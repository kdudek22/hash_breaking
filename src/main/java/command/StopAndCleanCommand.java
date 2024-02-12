package command;

public class StopAndCleanCommand implements Command{
    @Override
    public void execute() {
        StopHashBreakerCommand stopCommand = new StopHashBreakerCommand();
        stopCommand.execute();

        CleanCommand cleanCommand = new CleanCommand();
        cleanCommand.execute();
    }
}
