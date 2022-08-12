package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/21/18.
 */

public class AssignmentsBean {
    private String index;

    private String subjectuuid;

    private String subjectName;

    private String title;

    private String description;

    private String teacheruuid;

    private String teacherName;

    private String date;

    public AssignmentsBean(String index, String subjectuuid, String subjectName, String title, String description, String teacheruuid, String teacherName, String date) {
        this.index = index;
        this.subjectuuid = subjectuuid;
        this.subjectName = subjectName;
        this.title = title;
        this.description = description;
        this.teacheruuid = teacheruuid;
        this.teacherName = teacherName;
        this.date = date;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSubjectuuid() {
        return subjectuuid;
    }

    public void setSubjectuuid(String subjectuuid) {
        this.subjectuuid = subjectuuid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getTeacheruuid() {
        return teacheruuid;
    }

    public void setTeacheruuid(String teacheruuid) {
        this.teacheruuid = teacheruuid;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
