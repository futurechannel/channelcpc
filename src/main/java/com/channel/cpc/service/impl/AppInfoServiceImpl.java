package com.channel.cpc.service.impl;

import com.channel.cpc.dao.AppInfoDao;
import com.channel.cpc.entity.AppInfo;
import com.channel.cpc.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gq on 2018/4/29.
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {
    @Autowired
    private AppInfoDao appInfoDao;

    @Override
    public List<AppInfo> findAll() {
        return appInfoDao.findAll();
    }
}
