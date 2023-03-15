package com.example.hive.utils;

import java.util.Objects;

public class StringUtil {

    public static boolean doesBothStringMatch(String firstText, String secondText){
        if (Objects.nonNull(firstText) && Objects.nonNull(secondText)) {
            return Objects.equals(firstText, secondText);
        }
        return false;
    }
}
