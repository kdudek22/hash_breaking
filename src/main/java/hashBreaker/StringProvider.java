package hashBreaker;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringProvider {
    static long min = 'a' - 1;

    static long max = 'z' + 1;
    static long diff = max - min;

    public static String generateNextString(String lastString){
        return convertNumberToString(convertStringToNumber(lastString)+1);
    }

    public static long convertStringToNumber(String s){
        char[] arr = s.toCharArray();
        long indexMultiply = 1;
        long res = 0;

        for(int i = arr.length-1; i>=0; i--){
            res+=(arr[i]-min)*indexMultiply;
            indexMultiply*=diff;
        }
        return res;
    }

    public static String convertNumberToString(long value){
        List<Character> tmp = new ArrayList<>();
        while(value>0){
            long val = value%diff+min;
            char c = (char)(val);
            tmp.add(c);
            value/=diff;
        }
        Collections.reverse(tmp);
        StringBuilder builder = new StringBuilder(tmp.size());
        for(Character ch: tmp){
            builder.append(ch);
        }

        return builder.toString();
    }

    public static String getHashFromString(String input){
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
