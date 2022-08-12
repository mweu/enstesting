package com.benson.mansoft_charles.beans;

/**
 * Created by mansoft-charles on 6/22/18.
 */

public class StudentSubjectsBean {
    private String subjectId;

    private String subjectShortCode;

    private String subjectName;

    private String lastTermGrade;

    private Double lastTermMark;

    private Double thisTermMark;

    private String thisTermGrade;

    private Double targetMark;

    public StudentSubjectsBean(String subjectId, String subjectShortCode,
                               String subjectName, String lastTermGrade,
                               Double lastTermMark, Double thisTermMark,
                               String thisTermGrade,
                               Double targetMark) {
        this.subjectId = subjectId;
        this.subjectShortCode = subjectShortCode;
        this.subjectName = subjectName;
        this.lastTermGrade = lastTermGrade;
        this.lastTermMark = lastTermMark;
        this.thisTermMark = thisTermMark;
        this.thisTermGrade = thisTermGrade;
        this.targetMark = targetMark;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Double getTargetMark() {
        return targetMark;
    }

    public void setTargetMark(Double targetMark) {
        this.targetMark = targetMark;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setStudentId(String studentId) {
        this.subjectId = studentId;
    }

    public String getSubjectShortCode() {
        return subjectShortCode;
    }

    public void setSubjectShortCode(String subjectShortCode) {
        this.subjectShortCode = subjectShortCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLastTermGrade() {
        return lastTermGrade;
    }

    public void setLastTermGrade(String lastTermGrade) {
        this.lastTermGrade = lastTermGrade;
    }

    public Double getLastTermMark() {
        return lastTermMark;
    }

    public void setLastTermMark(Double lastTermMark) {
        this.lastTermMark = lastTermMark;
    }

    public Double getThisTermMark() {
        return thisTermMark;
    }

    public void setThisTermMark(Double thisTermMark) {
        this.thisTermMark = thisTermMark;
    }

    public String getThisTermGrade() {
        return thisTermGrade;
    }

    public void setThisTermGrade(String thisTermGrade) {
        this.thisTermGrade = thisTermGrade;
    }
}
