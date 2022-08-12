package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import com.benson.mansoft_charles.beans.TeachersBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mansoft-charles on 6/22/18.
 */

public interface SubjectsInterface {
    public void loadStudentSubjects(final AppCompatActivity context) throws JSONException;

    public String [][] getStudentSubjects(JSONObject value) throws JSONException;

    public void loadClassSubjects(final AppCompatActivity context, String subjectId) throws JSONException;

    public void loadClassSubjectTeachers(final AppCompatActivity context, String subjectId) throws JSONException;

    public TeachersBean getTeacherDetails(final AppCompatActivity context, String teacher);
}
