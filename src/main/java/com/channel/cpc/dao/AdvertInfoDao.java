package com.channel.cpc.dao;

import com.channel.cpc.entity.AdvertInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by gq on 2018/4/21.
 */
public interface AdvertInfoDao {

    List<AdvertInfo> findAll();

    AdvertInfo findById(@Param("adverterCode") String adverterCode, @Param("appCode") String appCode);

}
