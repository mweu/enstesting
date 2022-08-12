package com.benson.mansoft_charles.core;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.activities.EExamActivity;
import com.benson.mansoft_charles.beans.LoginCoreBean;
import com.benson.mansoft_charles.beans.SeverCoreBean;
import com.benson.mansoft_charles.beans.TeachersBean;
import com.benson.mansoft_charles.beans.TermDetailsBean;
import com.benson.mansoft_charles.interfaces.loginInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mansoft-charles on 6/18/18.
 */

public class Login extends AppCompatActivity implements loginInterface {


    @Override
    public void login(String url, final String userName, final String password, final AppCompatActivity context) throws Exception {
        if(ESSStaticVariables.showAuthentication == null || ESSStaticVariables.showAuthentication) {
            MainActivity.pd.setMessage("Authenticating ..");
            MainActivity.pd.setCancelable(false);
            MainActivity.pd.show();
            ESSStaticVariables.showAuthentication = false;
        }
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            parseLoginResults(new JSONObject(response), userName, password, context);
                        }
                        catch(JSONException ee) {
                            //Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    @Override
    public void parseLoginResults(JSONObject response, String upn, String password, final AppCompatActivity context) throws Exception {
       try {
           JSONObject res = response.getJSONObject("response");
           JSONObject data = res.getJSONObject("data");

           LoginCoreParameters obj = new LoginCoreParameters();
           obj.setSessionKey(data.getString("key"));
           obj.setTenantId(data.getString("companyId"));
           obj.setUserDisplay(data.getString("userPrincipalName"));
           obj.setUserId(data.getString("userId"));

           JSONObject properties = data.getJSONObject("userProp");
           ESSStaticVariables.session = obj;

           ESSStaticVariables.userDisplayHeader = "" + properties.getString("userDisplayName");

           LoginCoreBean dt = new LoginCoreBean(upn, password, data.getString("userId"));
           writeUserParameters(dt, context);
           loadTermDetails(context);
           loadTeachers(context);
           getStudentClass(context);
           loadSchoolAwards(context);
           loadDisciplineActions(context);
           showHomeScreen(context);
       }
       catch(Exception e)
       {
           Toast.makeText(context.getApplicationContext(), "Invalid User Id Or Password", Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public boolean writeUserParameters(LoginCoreBean data, final AppCompatActivity context) throws Exception {
        String datae = data.getUserId()+"," +data.getUserupn() +"," + data.getPassword();
        return new FilesMan().writeFile(datae, ESSStaticVariables.USER_CONFIG, context);
    }

    @Override
    public boolean writeServerParameters(SeverCoreBean data, final AppCompatActivity context) throws Exception {
        String datae = data.getProtocal()+"," +data.getDomain();
        return new FilesMan().writeFile(datae, ESSStaticVariables.SERVER_CONFIG, context);
    }

    @Override
    public boolean showLoginScreen(String path, AppCompatActivity context) throws Exception {
        SeverCoreBean server = new FilesMan().readServerCoreParams(ESSStaticVariables.SERVER_CONFIG, context);

        LoginCoreBean loginparams = new FilesMan().readUserCoreParams( ESSStaticVariables.USER_CONFIG, context);
        if(null == loginparams) {
            try {
                LoginCoreBean dt = new LoginCoreBean("1050@mansoftweb.com",
                        "Mansoft1234$", "User-Id");
                writeUserParameters(dt, context);
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }

        if(server == null)
        {
            String protocal  = "https";
            String domain  = "mansoftonline.com";
            SeverCoreBean obj = new SeverCoreBean(protocal, domain);
            try {
                writeServerParameters(obj, context);
                Intent intentdescriptive = new Intent(context, MainActivity.class);
                startActivity(intentdescriptive);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        else
        {
            if(new FilesMan().readFile(ESSStaticVariables.USER_CONFIG, context) == null)
            {
                return false;
            }
            else
            {
                String values = new FilesMan().readFile(ESSStaticVariables.USER_CONFIG, context);

                String params[] = values.split(",");
                if(params.length == 3)
                {
                    if(params[0].length() == 36)
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }


                }
                else
                {
                    return false;
                }
            }
        }

    }

    @Override
    public void setUpUserAccount(final AppCompatActivity context) throws Exception{
        ESSStaticVariables.CAN_GO_BACK = true;
        context.setContentView(R.layout.content_login);

        EditText header = (EditText) context.findViewById(R.id.header);
        header.setKeyListener(null);

        final EditText user = (EditText) context.findViewById(R.id.userName);
        final EditText password = (EditText) context.findViewById(R.id.password);
        Button login = (Button) context.findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upn = user.getText().toString();
                String pass = password.getText().toString();
                doLogin(upn, pass, context);

            }
        });
    }
    public void doLogin(String upn, String pass, AppCompatActivity context){
        SeverCoreBean obj = new FilesMan().readServerCoreParams(ESSStaticVariables.SERVER_CONFIG, context);
        ESSStaticVariables.BACKEND_ROOT_URL = obj.getProtocal() +"://"+obj.getDomain() +"/adminrs/core";
        String url = ESSStaticVariables.BACKEND_ROOT_URL +"/auth/ess/"+upn +"/"+pass;
        ESSStaticVariables.BACKEND_SCHOOL_URL = obj.getProtocal() +"://"+obj.getDomain() +"/schooladmin/core";
        try {
            login(url, upn, pass, context);
        } catch (UnknownHostException e) {
            try {
                new Login().setUpServerAccount(context);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpUserAccount(AppCompatActivity context, LoginCoreBean login) throws Exception {
        //ESSStaticVariables.CAN_GO_BACK = true;
        String upn = login.getUserupn();
        String pass = login.getPassword();
        SeverCoreBean obj = new FilesMan().readServerCoreParams(ESSStaticVariables.SERVER_CONFIG, context);
        ESSStaticVariables.BACKEND_ROOT_URL = obj.getProtocal() +"://"+obj.getDomain() +"/adminrs/core";
        ESSStaticVariables.BACKEND_SCHOOL_URL = obj.getProtocal() +"://"+obj.getDomain() +"/schooladmin/core";
        String url = obj.getProtocal() +"://"+obj.getDomain() +"/adminrs/core/auth/ess/"+upn +"/"+pass;
        try {
            login(url, upn, pass, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpServerAccount(final AppCompatActivity context) throws Exception{
        ESSStaticVariables.CAN_GO_BACK = true;
        context.setContentView(R.layout.server_setup);

        final  EditText server_domain = (EditText) context.findViewById(R.id.server_domain);

        final EditText transmission_protocal = (EditText) context.findViewById(R.id.transmission_protocal);

        SeverCoreBean obj = new FilesMan().readServerCoreParams(ESSStaticVariables.SERVER_CONFIG, context);
        if(obj!=null)
        {
            server_domain.setText(obj.getDomain());
            transmission_protocal.setText(obj.getProtocal());
        }
        Button save_server_setup = (Button) context.findViewById(R.id.save_server_setup);
        save_server_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String protocal  = transmission_protocal.getText().toString();
                String domain  = server_domain.getText().toString();
                SeverCoreBean obj = new SeverCoreBean(protocal, domain);
                try {
                    writeServerParameters(obj, context);
                    Intent intentdescriptive = new Intent(context, MainActivity.class);
                    startActivity(intentdescriptive);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showHomeScreen(final AppCompatActivity context) throws Exception {
        context.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);

        MainActivity.pd = new ProgressDialog(context);
        MainActivity.userName = (EditText)  context.findViewById(R.id.user_name);
       /* EditText appName = (EditText) context.findViewById(R.id.mainTitle);
        appName.setKeyListener(null);*/

        EditText user_name = (EditText) context.findViewById(R.id.user_name);
        user_name.setText(ESSStaticVariables.userDisplayHeader);
        user_name.setKeyListener(null);

        ActionBar actionbar = context.getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.homeindicator);



        Button fees = (Button) context.findViewById(R.id.fees);
        fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new FeesModule().loadFeesInterface(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button reports = (Button) context.findViewById(R.id.reports);
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new Reports().loadTermDetails(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button assignments = (Button) context.findViewById(R.id.assignments);
        assignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EExamActivity.class);
                context.startActivity(intent);
                /*try {
                    new Assignments().loadAssignments(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

        Button subjects = (Button) context.findViewById(R.id.subjects);
        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new SubjectsModule().loadStudentSubjects(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button awards = (Button) context.findViewById(R.id.awards);
        awards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new StudentTransactions().showStudentAwards(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button discipline = (Button) context.findViewById(R.id.discipline);
        discipline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new StudentTransactions().showStudentDiscipline(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button pastPapers = (Button) context.findViewById(R.id.pastpapers);
        pastPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new StudentTransactions().showPastPapers(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button alerts = (Button) context.findViewById(R.id.alerts);
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(context.getApplicationContext(), "Alerts... Coming Soon", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

       /* Button timetable = (Button) context.findViewById(R.id.timetablee);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(context.getApplicationContext(), "Timetable... Coming Soon", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/


        LoginCoreBean login = new FilesMan().readUserCoreParams(ESSStaticVariables.USER_CONFIG, context);
        String url = ESSStaticVariables.BACKEND_ROOT_URL +"/user/uphoto/"+
                login.getUserId().trim()+"/USER";
        loadUserImage(context, url);

    }

    @Override
    public void loadUserImage(final AppCompatActivity context ,String url) throws Exception {
        if(ESSStaticVariables.showAuthentication == null || ESSStaticVariables.showAuthentication) {
            MainActivity.pd.setMessage("Authenticating ..");
            MainActivity.pd.setCancelable(false);
            MainActivity.pd.show();
            ESSStaticVariables.showAuthentication = false;
        }
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            String photo = response;
                            String output = "";
                            if(photo.contains("data:image/jpeg"))
                            {
                                output = photo.replace("data:image/jpeg;base64,", "");
                            }
                            else if(photo.contains("data:image/png"))
                            {
                                output = photo.replace("data:image/png;base64,", "");
                            }
                            MainActivity.image= (ImageView) context.findViewById(R.id.imageViewToolBar);
                            String buffer = output.trim();
                            byte[] imageAsBytes = Base64.decode(buffer.getBytes(), 0);
                            Bitmap original = BitmapFactory.decodeByteArray(
                                    imageAsBytes, 0, imageAsBytes.length);
                            Bitmap bitmapImage = Bitmap.createScaledBitmap(original, 240,
                                    240, true);

                            MainActivity.image.setImageBitmap(Bitmap.createScaledBitmap(bitmapImage, 120, 120, false));
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                                    context.getResources(),
                                    bitmapImage
                            );
                            roundedBitmapDrawable.setCornerRadius(5.0f);
                            roundedBitmapDrawable.setAntiAlias(true);


                            MainActivity.image.setImageDrawable(roundedBitmapDrawable);
                        }
                        catch(Exception ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void loadTermDetails(final AppCompatActivity context) throws Exception {

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/school/getSchoolStudyPeridSession/"+
                ESSStaticVariables.session.getTenantId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }*/
                        try {
                           JSONObject values = new JSONObject(response);
                            JSONObject res = values.getJSONObject("response");
                            JSONArray data = res.getJSONArray("data");
                            JSONObject term = data.getJSONObject(0);
                            ESSStaticVariables.termSession = new TermDetailsBean(
                                    term.getString("mansoftltdTermId"),
                                    "",
                                    term.getString("mansoftltdSessionId")

                            );
                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }*/
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void loadTeachers(final AppCompatActivity context) throws Exception {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/school/listuser/Teacher/"+
                ESSStaticVariables.session.getTenantId();
        ESSStaticVariables.schoolTeachers = new ArrayList<TeachersBean>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }*/
                        try {
                            JSONObject values = new JSONObject(response);
                            JSONObject res = values.getJSONObject("response");
                            JSONArray data = res.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                JSONObject obj = data.getJSONObject(i);
                                ESSStaticVariables.schoolTeachers.add(new TeachersBean(
                                        obj.getString("userId"),
                                        obj.getString("pfNo"),
                                        obj.getString("displayName"),
                                        obj.getString("email"),
                                        obj.getString("mobileNo")
                                ));
                            }
                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }*/
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void getStudentClass(final AppCompatActivity context) throws Exception {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/school/getUserParameters/"+
                ESSStaticVariables.session.getUserId() +"/" + ESSStaticVariables.session.getTenantId() ;
        ESSStaticVariables.schoolTeachers = new ArrayList<TeachersBean>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }*/
                        try {
                            JSONObject values = new JSONObject(response);
                            JSONObject res = values.getJSONObject("response");
                            ESSStaticVariables.studentClass = res.getString("orgId");
                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }*/
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void loadSchoolAwards(final AppCompatActivity context) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/school/getSchoolAwards/"+
                ESSStaticVariables.session.getTenantId() ;
        ESSStaticVariables.studentAwards = new HashMap<String, String>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }*/
                        try {
                            JSONArray values = new JSONArray(response);
                            for(int i=0;i<values.length();i++)
                            {
                                JSONObject obj = values.getJSONObject(i);
                                ESSStaticVariables.studentAwards.put(obj.getString("awarduuid"), obj.getString("name"));
                            }
                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }*/
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void loadDisciplineActions(final AppCompatActivity context) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/school/getSchoolDisciplineAction/"+
                ESSStaticVariables.session.getTenantId();
        ESSStaticVariables.disciplineActions = new HashMap<String, String>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }*/
                        try {
                            JSONArray values = new JSONArray(response);
                            for(int i=0;i<values.length();i++)
                            {
                                JSONObject obj = values.getJSONObject(i);
                                ESSStaticVariables.disciplineActions.put(obj.getString("index").trim(), obj.getString("name").trim());
                            }
                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if ( MainActivity.pd.isShowing()) {
                    MainActivity.pd.dismiss();
                }*/
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);

    }
}
