package Nodes;

import hashBreaker.HashBreaker;
import hashBreaker.StringProvider;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        HashBreaker h = HashBreaker.getInstance();
//        Command startCommand = new StartHashBreakerCommand();
//        Command stopCommand = new StopHashBreakerCommand();
//        startCommand.execute();
//        stopCommand.execute();

        HashBreaker h = HashBreaker.getInstance();

        Scanner s = new Scanner(System.in);

        Node n = Node.getInstance();

        String message;
        while(true){
            message = s.nextLine();
            n.send(message);
        }

    }
}