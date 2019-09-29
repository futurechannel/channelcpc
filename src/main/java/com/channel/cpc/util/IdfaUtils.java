package com.channel.cpc.util;

import com.mifmif.common.regex.Generex;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IdfaUtils {

    public static String generateIdfa(){
        Generex generex = new Generex("([0-9A-F]{8})([-][0-9A-F]{4})([-][4][0-9A-F]{3})([-][0-9A-F]{4})([-][0-9A-F]{12})");
        return generex.random();
    }

    public static void main(String[] args) {
        try {
//            System.out.println(URLEncoder.encode("http://open.6to.com.cn:8282/capClickApi.php?idfa={0}&promoter={1}&callback={2}&adid=339&rtjson=1","UTF-8"));
            System.out.println(URLEncoder.encode("ip:ip","UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
