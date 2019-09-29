package com.channel.cpc.service.impl;

import com.channel.cpc.dao.AdvertInfoDao;
import com.channel.cpc.entity.AdvertInfo;
import com.channel.cpc.service.AdvertInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertInfoServiceImpl implements AdvertInfoService {
    @Autowired
    private AdvertInfoDao advertInfoDao;

    @Override
    public List<AdvertInfo> findAll() {
        return advertInfoDao.findAll();
    }

    @Override
    public AdvertInfo findById(String adverterCode, String appCode) {
        return advertInfoDao.findById(adverterCode, appCode);
    }
}
