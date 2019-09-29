package com.channel.cpc.service;

import com.channel.cpc.entity.AdvertInfo;

import java.util.List;

/**
 * Created by gq on 2018/4/16.
 */
public interface AdvertInfoService {

    List<AdvertInfo> findAll();

    AdvertInfo findById(String adverterCode,String appCode);

}
