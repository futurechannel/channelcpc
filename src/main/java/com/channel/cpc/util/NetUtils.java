package com.channel.cpc.util;


import com.channel.cpc.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * User:甘琪 DateTime: 2016/6/13.
 */
public class NetUtils {
    private NetUtils() {
    }

    /***
     * 获取ip地址
     *
     * @param request 请求
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isGetIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isGetIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isGetIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 是否获取到ip地址
     *
     * @return true表示能获取到
     */
    public static boolean isGetIp(String ip) {
        return (null == ip) || (ip.length() == 0) || (ip.equalsIgnoreCase(Constants.LOCAIL_IP)) || ("unknown".equalsIgnoreCase(ip));
    }

    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        map.put("key","wqdw");
        String keyWord=GsonUtils.pojoToJson(map);
        System.out.println(keyWord);
    }
}
