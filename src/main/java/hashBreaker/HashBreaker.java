package hashBreaker;

import java.security.MessageDigest;

public class HashBreaker {
    public static HashBreaker instance;
    public boolean isCurrentlyWorking = false;
    public String startString;
    public String endString;
    public boolean stop;

    MessageDigest encoder;

    public HashBreaker(){
        try{
            this.encoder = MessageDigest.getInstance("SHA-1");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public static HashBreaker getInstance(){
        if(instance == null){
            instance = new HashBreaker();
        }
        return instance;
    }

    public void startBreaker(String startString, String endString){
        this.startString = startString;
        this.endString = endString;
        this.stop = false;
//        this.isCurrentlyWorking = true;
    }
    public void stopBreaker(){
        this.stop = true;
//        this.isCurrentlyWorking = false;
    }

    public String breakHash(){
        while(!this.stop){

        }
        return "";
    }

    public String getHashFromString(String input){
        byte[] hashedBytes = this.encoder.digest(input.getBytes());

        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }

        return hexStringBuilder.toString();
    }

}
