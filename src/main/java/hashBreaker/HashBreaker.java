package hashBreaker;

import Nodes.Node;

import java.security.MessageDigest;

public class HashBreaker {
    public static HashBreaker instance;
    public String startString;
    public String endString;
    public String hashToFind;
    public boolean stop=true;

    public static int maxLetterCount = 8;

    public HashBreaker(){}
    public static HashBreaker getInstance(){
        if(instance == null){
            instance = new HashBreaker();
        }
        return instance;
    }

    public void startBreaker(String startString, String endString, String hashToFind){
        this.startString = startString;
        this.endString = endString;
        this.hashToFind = hashToFind;
        this.stop = false;
        new Thread(()->{
            this.breakHash();
        }).start();

    }

    public void stopBreaker(){
        this.stop = true;
    }

    public void breakHash(){
        System.out.println("=== STARTING HASH BREAKER, SOLVING " + this.startString + " - " + this.endString + " " + this.hashToFind + " ===");
        String currentString = this.startString;
        String currentStrigHash = StringProvider.getHashFromString(currentString);
        int currentIndex = 0;
        while(!this.stop && !currentStrigHash.equals(this.hashToFind) && !currentString.equals(this.endString) && currentString.length()<=maxLetterCount){
            if(currentIndex%2 == 11){
                System.out.println(currentString + " " + currentStrigHash + " " + this.hashToFind);
            }
            currentString = StringProvider.generateNextString(currentString);
            currentStrigHash = StringProvider.getHashFromString(currentString);
            currentIndex+=1;
        }
        if(currentString.length()>maxLetterCount){
            currentString = "BOUNDARY";
        }

        if(!this.hashToFind.equals(currentStrigHash) && !currentString.equals("BOUNDARY")){
            currentString = "";
        }

        Node node = Node.getInstance();
        node.callbackFromHashBreaker(currentString);
    }


}
