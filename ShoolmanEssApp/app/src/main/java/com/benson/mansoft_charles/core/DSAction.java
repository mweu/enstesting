package com.benson.mansoft_charles.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.DSParams;
import com.benson.mansoft_charles.interfaces.DSCallBack;


import org.json.JSONException;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DSAction<T, E>  extends AppCompatActivity {
    private String timeLabel;
    private Integer seconds;
    private Timer timer;
    private static boolean started = false;
    public void startExam(Integer timeInMinutes, AppCompatActivity context){
        try{
            if(null == ESSStaticVariables.examTimeInSeconds){
                ESSStaticVariables.examTimeInSeconds = ( timeInMinutes * 60 );
            }
           setTimer(new Timer());
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    setSeconds(ESSStaticVariables.examTimeInSeconds);
                    if(getSeconds() > 1){
                        setSeconds(getSeconds() - 1);
                        if(getSeconds() > 60){
                            Double minutesRemaining = Double.valueOf(getSeconds()) / 60.0;
                            int minutesRemainingC = minutesRemaining.intValue();
                            setTimeLabel("Rem. Time " + minutesRemainingC +" Min");
                        }else{
                            setTimeLabel("Rem. Time " + getSeconds() +" Sec");
                        }
                        ESSStaticVariables.examTimeInSeconds = getSeconds();


                    }else{
                        /*Force Submission Here*/
                    }
                }
            };
            if(!started) {
                getTimer().schedule(task, 0, 1000);
                started = true;
            }
        }catch(Exception e){
            Toast.makeText(context,e.toString(), Toast.LENGTH_LONG);
        }

    }
     public void makeGETRequest(String url, AppCompatActivity context, final DSCallBack<T, E> callBack) throws Exception{
        final ProgressDialog pd = new ProgressDialog(context);
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( pd.isShowing()) {
                            pd.dismiss();
                        }
                        try {
                            callBack.onResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {
                    callBack.onError(error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void makePOSTRequest(Context context, String url, final HashMap<String, String> params,final List<DSParams> headerParams,
                                       final DSCallBack<T, E> calback){
        final ProgressDialog pd = new ProgressDialog(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            calback.onResponse(response);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            calback.onError(error);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        )
        {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                //params.put("Content-Type","application/x-www-form-urlencoded");
                for(DSParams e : headerParams){
                    headers.put(e.getParameter(), e.getValue().toString());
                }
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);

    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
