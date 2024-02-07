package hashBreaker;

import Nodes.Node;

import java.security.MessageDigest;

public class HashBreaker {
    public static HashBreaker instance;
    public boolean isCurrentlyWorking = false;
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
        System.out.println("STARTING HASH BREAKER, SOLVING " + this.startString + " - " + this.endString + " " + this.hashToFind);
        String currentString = this.startString;
        String currentStrigHash = this.getHashFromString(currentString);
        int currentIndex = 0;
        while(!this.stop && !currentStrigHash.equals(this.hashToFind) && !currentString.equals(this.endString)){
            if(currentIndex%100000 == 0){
                System.out.println(currentString + " " + currentStrigHash + " " + this.hashToFind);
            }
            currentString = StringProvider.generateNextString(currentString);
            currentStrigHash = this.getHashFromString(currentString);
            currentIndex+=1;
        }

        if(!this.hashToFind.equals(currentStrigHash)){
            currentString = "";
        }
        Node node = Node.getInstance();
        node.callbackFromHashBreaker(currentString);
    }

    public String getHashFromString(String input){
        try{
            MessageDigest encoder = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = encoder.digest(input.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        }
        catch (Exception e){
            return "";
        }

    }

}
