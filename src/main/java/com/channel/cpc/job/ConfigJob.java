package com.channel.cpc.job;

import com.channel.cpc.constants.ConstantMaps;
import com.channel.cpc.dao.AdvertInfoDao;
import com.channel.cpc.dao.AppInfoDao;
import com.channel.cpc.entity.AdvertInfo;
import com.channel.cpc.entity.AppInfo;
import com.channel.cpc.util.ConfigUtils;
import com.channel.cpc.util.DateUtils;
import com.channel.cpc.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by gq on 2018/4/21.
 */
@Component
public class ConfigJob {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private AdvertInfoDao advertInfoDao;
    @Autowired
    private IpUtils ipUtils;

    @PostConstruct
    public void initConfig() {
        refreshConfig();
        loadIpCache();
    }

    /**
     * 每隔5分钟更新一次配置
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshConfig() {
        LOG.info("@Scheduled-----refreshConfig()");
        Map<String, AppInfo> tmpAppIdMap = new HashMap<>();
        Map<String, String> tmpAppCodeMap = new HashMap<>();

        Set<String> tmpAdvertSets = new HashSet<>();
        Map<String, Integer> tmpBalanceMap = new HashMap<>();
        Map<String, AdvertInfo> tmpAdvertInfoMap = new HashMap<>();


        Map<String, List<String>> tmpReportTables = new HashMap<>();
        Date date = new Date();
        List<String> tmpTables=new ArrayList<>();
        String tmpTable = ConfigUtils.getValue("report.table.prefix") + DateUtils.formatDate2Str(date, DateUtils.C_DATE_PATTON_YYYYMMDD);
        tmpTables.add(tmpTable);

        List<AppInfo> appInfos = appInfoDao.findAll();
        List<AdvertInfo> advertInfos = advertInfoDao.findAll();

        for (AppInfo appInfo : appInfos) {
            tmpAppCodeMap.put(appInfo.getAppCode(), appInfo.getAppId());
            tmpAppIdMap.put(appInfo.getAppId(), appInfo);
            List<String> reports = new ArrayList<>();
            //最多查七张表
            int tableNums = appInfo.getQueryTableNum() <= 7 ? appInfo.getQueryTableNum() : 7;
            for (int i = 0; i < tableNums; i++) {
                if (i == 0) {
                    reports.add(tmpTable);
                } else {
                    reports.add(ConfigUtils.getValue("report.table.prefix") + DateUtils.defineDayBefore2Str(date, -i, DateUtils.C_DATE_PATTON_YYYYMMDD));
                }
            }

            tmpReportTables.put(appInfo.getAppCode(), reports);
        }

        for (AdvertInfo advertInfo : advertInfos) {
            tmpAdvertSets.add(advertInfo.getAdverterCode());
            tmpBalanceMap.put(ConstantMaps.getBalanceKey(advertInfo.getAppCode(), advertInfo.getAdverterCode()), advertInfo.getBalanceRatio());
            tmpAdvertInfoMap.put(ConstantMaps.getBalanceKey(advertInfo.getAppCode(), advertInfo.getAdverterCode()), advertInfo);
        }

        if (!CollectionUtils.isEmpty(tmpAppCodeMap) && !CollectionUtils.isEmpty(tmpAppIdMap)) {
            ConstantMaps.setAppCodeMap(tmpAppCodeMap);
            ConstantMaps.setAppIdMap(tmpAppIdMap);
            ConstantMaps.setReportTables(tmpReportTables);
        }
        ConstantMaps.setReportTableName(tmpTables);


        if (!CollectionUtils.isEmpty(tmpAdvertSets) && !CollectionUtils.isEmpty(tmpBalanceMap)) {
            ConstantMaps.setAdvertSets(tmpAdvertSets);
            ConstantMaps.setBalanceMap(tmpBalanceMap);
            ConstantMaps.setAdvertInfoMap(tmpAdvertInfoMap);
        }

    }

    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void loadIpCache(){
        LOG.info("=================load ip cache begin===============");
        ipUtils.setIpCache();
        LOG.info("=================load ip cache end===============");
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void ipCacheLog(){
        List<String> ipCache = ipUtils.getIpCache();
        int size = ipCache == null ? 0 : ipCache.size();
        LOG.info("ip cache size : {}", size);
    }
}
