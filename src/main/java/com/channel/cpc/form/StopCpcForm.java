package com.channel.cpc.form;

import org.hibernate.validator.constraints.NotEmpty;

public class StopCpcForm {
    @NotEmpty
    private String appCode;
    @NotEmpty
    private String advertCode;

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

    @Override
    public String toString() {
        return "StopCpcForm{" +
                "appCode='" + appCode + '\'' +
                ", advertCode='" + advertCode + '\'' +
                '}';
    }
}
