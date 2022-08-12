package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

/**
 * Created by mansoft-charles on 6/20/18.
 */

public interface ReportsInterface {
    public void loadTermDetails(final AppCompatActivity context) throws JSONException, Exception;

    public String[][] getTermDetails(final AppCompatActivity context, String values) throws JSONException, Exception;

    public void openUrl(final AppCompatActivity context, final String url) throws Exception;
}
