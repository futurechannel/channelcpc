package com.channel.cpc.util;

/**
 * Created by gq on 2018/4/17.
 */
public class NumUtils {

    public static int randBoolean(int start, int end, int per) {

        int num = (int) (Math.random() * end) + start;
        if (num <= per) {
            return 1;
        }

        return 0;
    }

    public static int rand(int start,int end){
        return (int) (Math.random() * end) + start;
    }


}
