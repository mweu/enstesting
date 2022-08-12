package com.benson.mansoft_charles.core;

/**
 * Created by mansoft-charles on 5/3/18.
 */

import java.util.List;

public class LoginCoreParameters {
    private String userPricipleName;

    private String tenantId;

    private String sessionKey;

    private String photo;

    private String logo;

    private String userId;

    private String userDisplay;

    private List<String> licenses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPricipleName() {
        return userPricipleName;
    }

    public void setUserPricipleName(String userPricipleName) {
        this.userPricipleName = userPricipleName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

    public String getUserDisplay() {
        return userDisplay;
    }

    public void setUserDisplay(String userDisplay) {
        this.userDisplay = userDisplay;
    }
}
