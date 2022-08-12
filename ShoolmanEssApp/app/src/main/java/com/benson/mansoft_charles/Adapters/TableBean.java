package com.benson.mansoft_charles.Adapters;

public class TableBean {
    private int image ;
    private String name ;

    public int getImage() {
        return image;
    }

    public TableBean(String name, int image) {
        this.image = image;
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
