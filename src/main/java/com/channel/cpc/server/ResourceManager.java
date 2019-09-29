package com.channel.cpc.server;

import com.channel.cpc.dto.CpcReportDto;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ResourceManager {

    private final static ConcurrentMap<String, LinkedBlockingQueue<CpcReportDto>> cpcMap=new ConcurrentHashMap<>();


    public static ConcurrentMap<String, LinkedBlockingQueue<CpcReportDto>> getCpcMap() {
        return cpcMap;
    }



}
