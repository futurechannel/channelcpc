package com.channel.cpc.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * Created by gq on 2018/4/11.
 */
public class AddCpcForm {

    @NotEmpty
    private String appCode;
    @NotNull
    private Integer cpcNum;
    @NotEmpty
    private String reportUrl;
    private String token;
    @NotEmpty
    private String advertCode;
    private String otherParams;
    @NotEmpty
    private String from;


    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getCpcNum() {
        return cpcNum;
    }

    public void setCpcNum(Integer cpcNum) {
        this.cpcNum = cpcNum;
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

    public String getAdvertCode() {
        return advertCode;
    }

    public void setAdvertCode(String advertCode) {
        this.advertCode = advertCode;
    }

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
