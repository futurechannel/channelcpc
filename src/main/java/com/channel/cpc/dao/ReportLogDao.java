package com.channel.cpc.dao;

import com.channel.cpc.entity.ReportLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by gq on 2018/4/11.
 */
public interface ReportLogDao {

    int insert(@Param("log") ReportLog log, @Param("tableName") String tableName);

    List<ReportLog> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    ReportLog findById(@Param("id") String id, @Param("app") String app, @Param("tableName") String tableName);

}
