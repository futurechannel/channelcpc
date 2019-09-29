package com.channel.cpc.dao;

import com.channel.cpc.entity.AppInfo;

import java.util.List;

/**
 * Created by gq on 2018/4/21.
 */
public interface AppInfoDao {

    List<AppInfo> findAll();
}
