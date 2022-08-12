package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public class SeverCoreBean {

    private String protocal;

    private String domain;

    public SeverCoreBean(String protocal, String domain) {
        this.protocal = protocal;
        this.domain = domain;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
