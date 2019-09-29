package com.channel.cpc.handler;


import com.channel.cpc.constants.Constants;
import com.channel.cpc.dto.CpcReportDto;
import com.channel.cpc.entity.ReportLog;
import com.channel.cpc.enums.ErrorCode;
import com.channel.cpc.exception.ApiException;
import com.channel.cpc.service.ReportLogService;
import com.channel.cpc.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@Component("cpcReportHandler")
public class CpcReportHandler {

    private final Logger logger = Logger.getLogger(getClass());

    public static final String URL_PARAM_SEPARATOR = "&";

    @Autowired
    private ReportLogService logService;

    public void report(CpcReportDto cpcReportDto) {

        String appCode = cpcReportDto.getAppCode();
        String from = cpcReportDto.getFrom();
        String idfa = cpcReportDto.getIdfa();
        String reportUrl = cpcReportDto.getReportUrl();
        String otherParams = cpcReportDto.getOtherParams();
        String token = cpcReportDto.getToken();
        String advertCode = cpcReportDto.getAdvertCode();
        String ip = cpcReportDto.getIp();

        String callback;

        try {
            callback = URLEncoder.encode(ConfigUtils.getValue("our.callback.url")
                    + "idfa=" + idfa + URL_PARAM_SEPARATOR + "appcode=" + appCode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("encode 转码错误", e);
            throw new ApiException(ErrorCode.E901.getCode() + "");
        }

        long start = new Date().getTime();

        if (StringUtils.isEmpty(idfa) || StringUtils.isEmpty(from) || StringUtils.isEmpty(callback) || StringUtils.isEmpty(appCode) || StringUtils.isEmpty(reportUrl)) {
            logger.error("Forwarding request param error:[" + "idfa:" + idfa + " from:" + from + " callback:" + callback + " reportUrl:" + reportUrl + "]");
            throw new ApiException(ErrorCode.E902.getCode() + "");
        }

        //拼接url
        String url = StringFormatUtils.format(reportUrl, idfa, from, callback);

        if (!StringUtils.isEmpty(otherParams)) {
            Map<String, String> otherParamMap = StringFormatUtils.string2Map(otherParams);
            StringBuilder sb = new StringBuilder();
            if(otherParams.length()==1&&!StringUtils.isEmpty(ip)) {
                for (String key : otherParamMap.keySet()) {
                    sb.append("&").append(key).append("=");
                    sb.append(ip);
                }
            }

            url = url + sb.toString();
        }

        if (!StringUtils.isEmpty(token)) {
            url = url + "&" + token + "=" + Md5.Md5(from + idfa + Constants.JZFENHUO_GAMEID + Constants.JZFENHUO_SIGNKEY).toUpperCase();
        }

        //上报记录入库
        ReportLog log = new ReportLog();
        log.setIdfa(idfa);
        log.setAppCode(appCode);
        log.setAdverterCode(advertCode);
        log.setReportTime(new Date());
        log.setIsCpcReport(1);

        int i = logService.insert(log);

        long middle = new Date().getTime();

        if (i < 1) {
            throw new ApiException(ErrorCode.E701.getCode() + "");
        }

        //转发请求给应用

        String resStr = HttpClientUtil.httpGet(url);
        if (StringUtils.isEmpty(resStr)) {
            logger.error("report error:[" + "url:" + url + "]");
            throw new ApiException(ErrorCode.E901.getCode() + "");
        }

        logger.info("Forwarding request:[" + " resStr:" + resStr + "url:" + url + "]");

        logger.info("appCode:" + appCode + ",总耗时:" + (new Date().getTime() - start) + "ms,入库耗时:" + (middle - start) + "ms");

    }

}
