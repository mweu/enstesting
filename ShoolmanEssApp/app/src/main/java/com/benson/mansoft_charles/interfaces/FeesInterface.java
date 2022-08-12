package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mansoft-charles on 6/19/18.
 */

public interface FeesInterface {
    public void loadFeesInterface(final AppCompatActivity context) throws JSONException;

    public String[][] getStudentFeeData(JSONObject data, final AppCompatActivity context) throws JSONException;
}
