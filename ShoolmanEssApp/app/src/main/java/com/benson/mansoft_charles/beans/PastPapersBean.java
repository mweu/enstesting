package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/26/18.
 */

public class PastPapersBean {

    private String index;

    private String title;

    private String description;

    public PastPapersBean(String index, String title, String description) {
        this.index = index;
        this.title = title;
        this.description = description;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
