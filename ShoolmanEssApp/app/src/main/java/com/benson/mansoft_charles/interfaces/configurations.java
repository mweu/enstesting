package com.benson.mansoft_charles.interfaces;

import android.support.v7.app.AppCompatActivity;

import com.benson.mansoft_charles.beans.LoginCoreBean;
import com.benson.mansoft_charles.beans.SeverCoreBean;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public interface configurations {
    public boolean writeFile(String data, String path, final AppCompatActivity context);

    public String readFile(String path, final AppCompatActivity context);

    public LoginCoreBean readUserCoreParams(String path, final AppCompatActivity context);

    public SeverCoreBean readServerCoreParams(String path, final AppCompatActivity context);
}
