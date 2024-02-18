package utils;

public class SleepUtils {
    public static void sleepTimeAmount(int timeAmount){
        try{
            Thread.sleep(timeAmount);
        }
        catch (InterruptedException e){
            System.out.println("INTERRUPTED");
        }
    }
}
