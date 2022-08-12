package com.benson.mansoft_charles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.benson.mansoft_charles.beans.DSParams;
import com.benson.mansoft_charles.core.DSAction;
import com.benson.mansoft_charles.core.ESSStaticVariables;
import com.benson.mansoft_charles.enums.QuestionType;
import com.benson.mansoft_charles.interfaces.DSCallBack;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StartNewExamActivity extends AppCompatActivity {
    private static EditText minutes;
    private static EditText selectedExam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exam_ui);
        EditText label = (EditText) findViewById(R.id.timeInMinutesLabel);
        label.setKeyListener(null);

        minutes = (EditText) findViewById(R.id.timeInMinutes);
        minutes.setKeyListener(null);

        selectedExam = (EditText) findViewById(R.id.selectedExam);
        selectedExam.setKeyListener(null);

        Button start = (Button) findViewById(R.id.beginExam);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getExamDetails/"+
                EExamActivity.selectedExam.getName();

        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, StartNewExamActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            selectedExam.setText(new JSONObject(response).getString("mansoftltdDesc"));
                            minutes.setText(new JSONObject(response).getInt("mansoftltdTimeMinutes") +" Minutes");
                            if(null == ESSStaticVariables.startedExams.get(EExamActivity.selectedExam.getName())){
                                ESSStaticVariables.startedExams.put(EExamActivity.selectedExam.getName(), true);
                                ESSStaticVariables.examTimeInSeconds = new JSONObject(response).getInt("mansoftltdTimeMinutes") * 60;
                            }else if(!ESSStaticVariables.startedExams.get(EExamActivity.selectedExam.getName())){
                                ESSStaticVariables.startedExams.put(EExamActivity.selectedExam.getName(), true);
                                ESSStaticVariables.examTimeInSeconds = new JSONObject(response).getInt("mansoftltdTimeMinutes") * 60;
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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getStudentExamStatus/"+
                        EExamActivity.selectedExam.getName() +"/" + ESSStaticVariables.session.getUserId();
                try {
                    new DSAction<JSONObject, String>().makeGETRequest(url, StartNewExamActivity.this,
                            new DSCallBack<JSONObject, String>() {
                                @Override
                                public JSONObject onResponse(String response) throws Exception {
                                    Log.e("",response);
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("status")){
                                        Toast.makeText(StartNewExamActivity.this,obj.getString("message"),
                                                Toast.LENGTH_LONG).show();
                                    }else{
                                        startExam();
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
        });


    }

    public void startExam(){
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/startStudentExamStatus/"+
                EExamActivity.selectedExam.getName() +"/" + ESSStaticVariables.session.getUserId() +"/" +
                ESSStaticVariables.session.getTenantId();
        new DSAction<String, String>().makePOSTRequest(StartNewExamActivity.this, url, new HashMap<String, String>(), new ArrayList<DSParams>(),
                new DSCallBack<String, String>() {
                    @Override
                    public String onResponse(String response) throws Exception {
                        if(response.trim().length() == 36){
                            ESSStaticVariables.examSession = response;
                            getQuestion();
                        }
                        return null;
                    }

                    @Override
                    public String onError(VolleyError error) throws Exception {
                        return null;
                    }
                });

    }

    public void getQuestion(){
        ESSStaticVariables.questionNo = 1;
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getExamQuestion/"+
                EExamActivity.selectedExam.getName() +"/" +
                ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.questionNo;
        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, StartNewExamActivity.this,
                    new DSCallBack<JSONObject, String>() {
                        @Override
                        public JSONObject onResponse(String response) throws Exception {
                            JSONObject question = new JSONObject(response);
                            QuestionType type = QuestionType.valueOf(question.getString("questionType"));
                            ESSStaticVariables.currentQuestion = question;
                            if(null == ESSStaticVariables.examAction){
                                ESSStaticVariables.examAction = new DSAction<>();
                            }
                            if(null == ESSStaticVariables.startedExams.get(EExamActivity.selectedExam.getName())){
                                ESSStaticVariables.startedExams.put(EExamActivity.selectedExam.getName(), true);
                                ESSStaticVariables.examDescription = selectedExam.getText().toString();
                            }else if(!ESSStaticVariables.startedExams.get(EExamActivity.selectedExam.getName())){
                                ESSStaticVariables.startedExams.put(EExamActivity.selectedExam.getName(), true);
                                ESSStaticVariables.examDescription = selectedExam.getText().toString();
                            }
                            switch(type){
                                case BOOLEAN:
                                    Intent intent = new Intent(StartNewExamActivity.this, BooleanQuestionActivity.class);
                                    startActivity(intent);
                                    break;
                                case DESCRIPTIVE:
                                    Intent intentDescriptive = new Intent(StartNewExamActivity.this, DescriptiveQuestionActivity.class);
                                    startActivity(intentDescriptive);
                                    break;
                                case MULTIPLE_CHOICES:
                                    Intent intentMultiple = new Intent(StartNewExamActivity.this, MultipleChoicesActivity.class);
                                    startActivity(intentMultiple);
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
}
