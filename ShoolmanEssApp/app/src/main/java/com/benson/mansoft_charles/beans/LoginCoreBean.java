package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public class LoginCoreBean {
    private String userupn;

    private String password;

    private String userId;

    public LoginCoreBean(String userupn, String password, String userId) {
        this.userupn = userupn;
        this.password = password;
        this.userId = userId;
    }

    public String getUserupn() {
        return userupn;
    }

    public void setUserupn(String userupn) {
        this.userupn = userupn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
