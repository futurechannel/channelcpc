package com.channel.cpc.dto;

public class CpcReportDto {

    private String appCode;
    private String advertCode;
    private String reportUrl;
    private String token;
    private String otherParams;
    private String idfa;
    private String ip;
    private String from;
    private String ourCallBackUrl;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAdvertCode() {
        return advertCode;
    }

    public void setAdvertCode(String advertCode) {
        this.advertCode = advertCode;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOurCallBackUrl() {
        return ourCallBackUrl;
    }

    public void setOurCallBackUrl(String ourCallBackUrl) {
        this.ourCallBackUrl = ourCallBackUrl;
    }

    @Override
    public String toString() {
        return "CpcReportDto{" +
                "appCode='" + appCode + '\'' +
                ", advertCode='" + advertCode + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                ", token='" + token + '\'' +
                ", otherParams='" + otherParams + '\'' +
                ", idfa='" + idfa + '\'' +
                ", ip='" + ip + '\'' +
                ", from='" + from + '\'' +
                ", ourCallBackUrl='" + ourCallBackUrl + '\'' +
                '}';
    }
}
