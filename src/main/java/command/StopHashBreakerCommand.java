package command;

import hashBreaker.HashBreaker;

public class StopHashBreakerCommand implements Command{
    @Override
    public void execute() {
        HashBreaker h = HashBreaker.getInstance();
        h.stopBreaker();
    }
}
