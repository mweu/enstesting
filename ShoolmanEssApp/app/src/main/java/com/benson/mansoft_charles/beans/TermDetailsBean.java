package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/19/18.
 */

public class TermDetailsBean {
    private String termId;

    private String termName;

    private String termSession;

    public TermDetailsBean(String termId, String termName, String termSession) {
        this.termId = termId;
        this.termName = termName;
        this.termSession = termSession;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermSession() {
        return termSession;
    }

    public void setTermSession(String termSession) {
        this.termSession = termSession;
    }
}
