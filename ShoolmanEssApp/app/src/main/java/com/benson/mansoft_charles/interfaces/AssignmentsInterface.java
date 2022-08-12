package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

/**
 * Created by mansoft-charles on 6/21/18.
 */

public interface AssignmentsInterface {
    public void loadAssignments(final AppCompatActivity context) throws Exception, JSONException;
}
