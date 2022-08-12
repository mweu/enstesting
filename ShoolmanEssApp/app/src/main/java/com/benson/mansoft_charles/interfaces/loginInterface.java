package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import com.benson.mansoft_charles.beans.LoginCoreBean;
import com.benson.mansoft_charles.beans.SeverCoreBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public interface loginInterface {
    public void login(String url, String userName, String password, final AppCompatActivity context) throws Exception;

    public void parseLoginResults(final JSONObject response,  String userName, String password, final AppCompatActivity context) throws Exception;

    public boolean writeUserParameters(LoginCoreBean data, final AppCompatActivity context) throws Exception;

    public boolean writeServerParameters(SeverCoreBean data, final AppCompatActivity context) throws Exception;

    public boolean showLoginScreen(String path, final AppCompatActivity context) throws Exception;

    public void setUpUserAccount(final AppCompatActivity context) throws Exception;

    public void setUpUserAccount(final AppCompatActivity context, LoginCoreBean login) throws Exception;

    public void setUpServerAccount(final AppCompatActivity context) throws Exception;

    public void showHomeScreen(final AppCompatActivity context) throws Exception;

    public void loadUserImage(final AppCompatActivity context, String url) throws Exception;

    public void loadTermDetails(final AppCompatActivity context) throws Exception;

    public void loadTeachers(final AppCompatActivity context) throws Exception;

    public void getStudentClass(final AppCompatActivity context) throws Exception;

    public void loadSchoolAwards(final AppCompatActivity context) throws JSONException;

    public void loadDisciplineActions(final AppCompatActivity context) throws JSONException;
}
