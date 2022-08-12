package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/19/18.
 */

public class StudentFeesBean {
    private String accountCode;

    private String accountName;

    private Double balanceBf;

    private Double outstating;

    private Double total;

    public StudentFeesBean(String accountCode, String accountName, Double balanceBf, Double outstating, Double total) {
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.balanceBf = balanceBf;
        this.outstating = outstating;
        this.total = total;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getBalanceBf() {
        return balanceBf;
    }

    public void setBalanceBf(Double balanceBf) {
        this.balanceBf = balanceBf;
    }

    public Double getOutstating() {
        return outstating;
    }

    public void setOutstating(Double outstating) {
        this.outstating = outstating;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
