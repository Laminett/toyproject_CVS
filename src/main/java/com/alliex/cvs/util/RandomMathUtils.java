package com.alliex.cvs.util;

import java.util.Random;

public class RandomMathUtils {
    public static String createTransNumber(){
        StringBuffer temp = new StringBuffer();
        Random randomNumber = new Random();
        for(int i = 0 ; i<12; i++){
            int randomIndex = randomNumber.nextInt(3);
            switch (randomIndex){
                case 0 :
                    //a-z
                    temp.append((char)((int)(randomNumber.nextInt(26))+97));
                    break;
                case 1 :
                    //A-Z
                    temp.append((char)((int)(randomNumber.nextInt(26))+65));
                    break;
                case 2 :
                    temp.append((randomNumber.nextInt(10)));
                    break;

            }
        }

        return temp.toString();
    }
}
