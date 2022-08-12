package com.benson.mansoft_charles.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.benson.mansoft_charles.beans.DSParams;
import com.benson.mansoft_charles.core.DSAction;
import com.benson.mansoft_charles.core.ESSStaticVariables;
import com.benson.mansoft_charles.enums.QuestionType;
import com.benson.mansoft_charles.interfaces.DSCallBack;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DescriptiveQuestionActivity extends AppCompatActivity {
    private EditText mainLabel;
    private EditText explation;
    private Button submit;
    private CardView submitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptive_questions);
        mainLabel = (EditText) findViewById(R.id.selectedExam);
        explation = (EditText) findViewById(R.id.questionExplanation);
        mainLabel.setKeyListener(null);
        ESSStaticVariables.examAction.startExam(ESSStaticVariables.examTimeInSeconds, DescriptiveQuestionActivity.this);

        String question = null;
        try {
            question = ESSStaticVariables.currentQuestion.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebView webview = (WebView) findViewById(R.id.question);
        webview.getSettings().setJavaScriptEnabled(true);
        WebSettings mWebSettings = webview.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webview.setScrollBarSize(10);
        webview.setScrollbarFadingEnabled(false);
        webview.loadDataWithBaseURL("", question, "text/html", "UTF-8", "");

        mainLabel.setText(ESSStaticVariables.examAction.getTimeLabel());
        getAnswers();

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ESSStaticVariables.questionNo  > 1 ){
                    ESSStaticVariables.questionNo = ESSStaticVariables.questionNo - 1;
                }
                ESSStaticVariables.currentQuestion =  ESSStaticVariables.previousQuestion;
                finish();
                /*Intent intent = new Intent(BooleanQuestionActivity.this, StartNewExamActivity.class);
                startActivity(intent);*/

            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitQuestion();
            }
        });

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DescriptiveQuestionActivity.this)
                        .setTitle("Submit Exam")
                        .setMessage("Are you sure you want to submit the exam?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                submitExam();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        submitView = (CardView) findViewById(R.id.levelFour);
        detectLastQuestion();
        /*view.removeView(submit);*/

    }

    private void submitQuestion(){
        try {
            String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/submitDescriptiveQuestion/"+
                    ESSStaticVariables.currentQuestion.getString("index");
            List<DSParams> headers = new ArrayList<>();
            headers.add(new DSParams("sessionKey", ESSStaticVariables.examSession));
            HashMap<String, String> params = new HashMap<>();
            params.put("explanation", explation.getText().toString());
            ESSStaticVariables.examAction.makePOSTRequest(DescriptiveQuestionActivity.this,
                    url, params, headers, new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            getNextQuestion();
                            return null;
                        }

                        @Override
                        public String onError(VolleyError error) throws Exception {
                            return null;
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getAnswers(){
        try {
            String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getDescriptiveAnswer/"+
                    ESSStaticVariables.currentQuestion.getString("index") + "/" +
                    ESSStaticVariables.examSession;
            new DSAction<JSONObject, String>().makeGETRequest(url, DescriptiveQuestionActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            JSONObject obj = new JSONObject(response);
                            explation.setText(obj.getString("description"));
                            return null;
                        }

                        @Override
                        public String onError(VolleyError error) throws Exception {
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitExam(){
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/submitExam/"+
                EExamActivity.selectedExam.getName()+ "/" +
                ESSStaticVariables.examSession;
        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, DescriptiveQuestionActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            if(Boolean.valueOf(response)){
                                Toast.makeText(DescriptiveQuestionActivity.this, "Exam was submited successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DescriptiveQuestionActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DescriptiveQuestionActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                            return null;
                        }

                        @Override
                        public String onError(VolleyError error) throws Exception {
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNextQuestion(){
        ESSStaticVariables.questionNo = ESSStaticVariables.questionNo + 1;
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getExamQuestion/"+
                EExamActivity.selectedExam.getName() +"/" +
                ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.questionNo;
        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, DescriptiveQuestionActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            JSONObject question = new JSONObject(response);
                            QuestionType type = QuestionType.valueOf(question.getString("questionType"));
                            ESSStaticVariables.previousQuestion =  ESSStaticVariables.currentQuestion;
                            ESSStaticVariables.currentQuestion = question;
                            if(null == ESSStaticVariables.examAction){
                                ESSStaticVariables.examAction = new DSAction<>();
                            }

                            switch(type){
                                case BOOLEAN:
                                    Intent intent = new Intent(DescriptiveQuestionActivity.this, BooleanQuestionActivity.class);
                                    startActivity(intent);
                                    break;
                                case DESCRIPTIVE:
                                    Intent intentdescriptive = new Intent(DescriptiveQuestionActivity.this, DescriptiveQuestionActivity.class);
                                    startActivity(intentdescriptive);
                                    break;
                                case MULTIPLE_CHOICES:
                                    Intent intentmultiple = new Intent(DescriptiveQuestionActivity.this, MultipleChoicesActivity.class);
                                    startActivity(intentmultiple);
                                    break;
                            }
                            return null;
                        }

                        @Override
                        public String onError(VolleyError error) throws Exception {
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detectLastQuestion(){
        Log.e("","Detecting Last Question");
        int currentQuestion = ESSStaticVariables.questionNo;
        int nextQuestion = currentQuestion + 1;
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getExamQuestion/"+
                EExamActivity.selectedExam.getName() +"/" +
                ESSStaticVariables.session.getTenantId() +"/" + nextQuestion;
        Log.e("",url);
        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, DescriptiveQuestionActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            if(response !=null){
                                try {
                                    JSONObject question = new JSONObject(response);
                                    if(question!=null){
                                        Log.e("","Removing submit button");
                                        submitView.removeView(submit);
                                    }
                                }
                                catch(Exception e){

                                }
                            }


                            return null;
                        }

                        @Override
                        public String onError(VolleyError error) throws Exception {
                            return null;
                        }
                    });
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        if(ESSStaticVariables.questionNo  > 1 ){
            ESSStaticVariables.questionNo = ESSStaticVariables.questionNo - 1;
        }
        ESSStaticVariables.currentQuestion =  ESSStaticVariables.previousQuestion;
        finish();
    }

}
