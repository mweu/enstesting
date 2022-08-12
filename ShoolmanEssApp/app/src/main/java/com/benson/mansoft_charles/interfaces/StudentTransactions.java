package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mansoft-charles on 6/25/18.
 */

public interface StudentTransactions {
    public void showStudentAwards(final AppCompatActivity context) throws JSONException;

    public String[][] getStudentAwards(JSONObject value) throws JSONException;

    public void showStudentDiscipline(final AppCompatActivity context) throws JSONException;

    public String[][] getStudentDiscipline(JSONObject value, final AppCompatActivity context) throws JSONException;

    public void showPastPapers(final AppCompatActivity context) throws JSONException;

    public String[][] getStudentPastPapers(JSONObject value, final AppCompatActivity context) throws JSONException;

    public void loadSharing(final AppCompatActivity context) throws Exception;

    public void performLogOutTask(
            final AppCompatActivity context,
            final String title,
            final String message);



}
