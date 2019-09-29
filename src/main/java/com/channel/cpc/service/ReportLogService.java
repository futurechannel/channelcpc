package com.channel.cpc.service;

import com.channel.cpc.entity.ReportLog;

/**
 * Created by gq on 2018/4/13.
 */

public interface ReportLogService {

    int insert(ReportLog log);

    ReportLog findById(String idfa, String appcode);

}
