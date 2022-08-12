package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/26/18.
 */

public class StudentDisciplineBean {

    private String comments;

    private String action;

    private String date;

    private String author;

    public StudentDisciplineBean(String comments, String action, String date, String author) {
        this.comments = comments;
        this.action = action;
        this.date = date;
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
