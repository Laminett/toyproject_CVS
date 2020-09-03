package com.alliex.cvs.util;

import org.apache.commons.lang3.RandomStringUtils;


public class RandomMathUtils {
    public static String createTransNumber(){
        final int RANDOM_STRING_LENGTH = 12;

        return RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
    }
}
