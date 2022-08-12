package com.benson.mansoft_charles.core;

import android.support.v7.app.AppCompatActivity;

import com.benson.mansoft_charles.beans.LoginCoreBean;
import com.benson.mansoft_charles.beans.SeverCoreBean;
import com.benson.mansoft_charles.interfaces.configurations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public class FilesMan implements configurations {
    @Override
    public boolean writeFile(String data,
                             String path,
                             final AppCompatActivity context) {
        FileOutputStream fileOut = null;
         try {
                fileOut = context.openFileOutput(path, context.MODE_PRIVATE);
                fileOut.write(data.getBytes());
                fileOut.close();
            }
            catch(Exception e)
            {

            }

        return true;
    }

    @Override
    public String readFile(String path, AppCompatActivity context) {
        String Message;
        try {
            FileInputStream fileIn = context.openFileInput(path);
            InputStreamReader reader = new InputStreamReader(fileIn);
            BufferedReader bufferReader = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer();
            while((Message = bufferReader.readLine()) !=null)
            {
                buffer.append(Message +"\n");
            }
            return buffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LoginCoreBean readUserCoreParams(String path, AppCompatActivity context) {
        String response = readFile(path, context);
        if(response == null)
        {
            return null;
        }
        else
        {
            String values [] = response.split(",");
            LoginCoreBean obj = new LoginCoreBean(values[1].trim(), values[2].trim(), values[0].trim());
            return obj;
        }
    }

    @Override
    public SeverCoreBean readServerCoreParams(String path, AppCompatActivity context) {
        String response = readFile(path, context);
        if(response == null)
        {
            return null;
        }
        else
        {
            try{
            String values [] = response.split(",");
            String protocal = values[0].trim();
            String domain = values[1].trim();
            SeverCoreBean obj = new SeverCoreBean(protocal, domain);
            return obj;
            }catch(Exception e){
                return null;
            }

        }
    }


}
