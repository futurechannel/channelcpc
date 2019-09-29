package com.channel.cpc.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class StartCpcForm {

    @NotEmpty
    private String appCode;
    @NotEmpty
    private String advertCode;
    @NotNull
    private Integer cpcNum;
    @NotNull
    private Integer cpcTime;

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

    public Integer getCpcNum() {
        return cpcNum;
    }

    public void setCpcNum(Integer cpcNum) {
        this.cpcNum = cpcNum;
    }

    public Integer getCpcTime() {
        return cpcTime;
    }

    public void setCpcTime(Integer cpcTime) {
        this.cpcTime = cpcTime;
    }
}
