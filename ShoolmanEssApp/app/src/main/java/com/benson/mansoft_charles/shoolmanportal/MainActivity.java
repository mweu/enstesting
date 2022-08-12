package com.benson.mansoft_charles.shoolmanportal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.benson.mansoft_charles.beans.LoginCoreBean;
import com.benson.mansoft_charles.core.ESSStaticVariables;
import com.benson.mansoft_charles.core.EventsSwitch;
import com.benson.mansoft_charles.core.FilesMan;
import com.benson.mansoft_charles.core.Login;
import com.benson.mansoft_charles.shoolmanportal.R;


import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static ActionBarDrawerToggle mToggle;
    public static EditText userName;
    public static ProgressDialog pd;
    public static ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initiateVariables(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initiateVariables(AppCompatActivity context) throws Exception {
        context.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);

        pd = new ProgressDialog(MainActivity.this);
        userName = (EditText)  context.findViewById(R.id.user_name);
        /*EditText appName = (EditText) context.findViewById(R.id.mainTitle);
        appName.setKeyListener(null);*/
        if(new Login().showLoginScreen(ESSStaticVariables.USER_CONFIG, MainActivity.this)){
                new Login().setUpUserAccount(MainActivity.this);
            }
        else {
            LoginCoreBean params = new FilesMan().readUserCoreParams( ESSStaticVariables.USER_CONFIG, MainActivity.this);
            if(params!=null) {
                new Login().doLogin(params.getUserupn(), params.getPassword(), MainActivity.this);
            }
        }


}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.severSetup:
                try {
                    new Login().setUpServerAccount(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.login:
                try {
                    new Login().setUpUserAccount(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.share:
                    try{
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        String message = "Download SCHOOLMAN parent portal from \n \n" +
                                "https://play.google.com/store/apps/details?id=com.benson.mansoft_charles.shoolmanportal";
                        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(ESSStaticVariables.CAN_GO_BACK)
        {
            ESSStaticVariables.CAN_GO_BACK = false;
            try {

                if(null ==  ESSStaticVariables.selectedTask)
                {
                    initiateVariables(MainActivity.this);
                }
                else {
                    new EventsSwitch().swichSchoolIntent(MainActivity.this, ESSStaticVariables.selectedTask);
                    ESSStaticVariables.selectedTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.exit(0);
        }
    }
}
