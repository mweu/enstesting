package com.benson.mansoft_charles.beans;

public class DSParams {
    private String parameter;

    private Object value;

    public DSParams(String parameter, Object value) {
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
