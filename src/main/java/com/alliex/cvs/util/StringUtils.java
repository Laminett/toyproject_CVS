package com.alliex.cvs.util;


public class StringUtils {

    public static String makeLike(String str) {
        if (str == null) {
            return null;
        } else {
            return "%" + str.trim() + "%";
        }
    }

}