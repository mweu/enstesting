package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/22/18.
 */

public class TeachersBean {
    private String userId;

    private String pfName;

    private String name;

    private String email;

    private String mobile;

    public TeachersBean(String userId, String pfName, String name, String email, String mobile) {
        this.userId = userId;
        this.pfName = pfName;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPfName() {
        return pfName;
    }

    public void setPfName(String pfName) {
        this.pfName = pfName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
