package command;

import hashBreaker.HashBreaker;

public class StartHashBreakerCommand implements Command{

    @Override
    public void execute() {
        HashBreaker h = HashBreaker.getInstance();
        h.startBreaker("a","zzzz");
    }
}
