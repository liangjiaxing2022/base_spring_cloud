package cn.shendu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="securityconfig")
public class SecuritySettings {
    private String permitall = "/api";
    private String urlroles;


    public String getPermitall() {
        return permitall;
    }

    public void setPermitall(String permitall) {
        this.permitall = permitall;
    }


    public String getUrlroles() {
        return urlroles;
    }

    public void setUrlroles(String urlroles) {
        this.urlroles = urlroles;
    }
}
