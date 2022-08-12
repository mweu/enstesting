package com.benson.mansoft_charles.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.benson.mansoft_charles.Adapters.FactoryAdapter;
import com.benson.mansoft_charles.core.DSAction;
import com.benson.mansoft_charles.interfaces.DSCallBack;
import com.benson.mansoft_charles.Adapters.TableBean;
import com.benson.mansoft_charles.core.ESSStaticVariables;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EExamActivity extends AppCompatActivity implements FactoryAdapter.OnItemListener {
    public RecyclerView recyclerView ;
    public LinearLayoutManager layoutManager ;
    public FactoryAdapter factoryAdapter ;
    public static List<TableBean> examsList ;
    public static List<TableBean>examsIndexes ;
    public static TableBean selectedExam;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        examsList = new ArrayList<>();
        examsIndexes = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");

        TextView label  = findViewById(R.id.toolBarLabel);
        label.setText("Available Exams");
        dialog.show();

        String url = ESSStaticVariables.BACKEND_SCHOOL_URL + "/schooltransactions/getStudentCurrentEExams/"+
                ESSStaticVariables.session.getUserId() + "/" + ESSStaticVariables.session.getTenantId() + "/" +
                ESSStaticVariables.termSession.getTermId();
        try {
            new DSAction<JSONObject, String>().makeGETRequest(url, EExamActivity.this, new DSCallBack<JSONObject, String>() {
                @Override
                public JSONObject onResponse(String response) throws Exception {
                    Log.println(Log.ERROR,"", response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject res = obj.getJSONObject("response");
                    JSONArray data = res.getJSONArray("data");
                    for(int i= 0;i<data.length();i++){
                        JSONObject val = data.getJSONObject(i);
                        examsList.add(new TableBean(val.getString("mansoftltdDesc"), R.drawable.pastpapers));
                        examsIndexes.add(new TableBean(val.getString("mansoftltdIndex"), R.drawable.pastpapers));
                    }
                    factoryAdapter = new FactoryAdapter(EExamActivity.this, examsList, EExamActivity.this);
                    recyclerView.setAdapter(factoryAdapter);
                    dialog.hide();
                    return null;
                }

                @Override
                public String onError(VolleyError error) throws Exception {
                    error.printStackTrace();
                    dialog.hide();
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TableBean> getTestExams() {
        List<TableBean> factories = new ArrayList<>();
        factories.add(new TableBean("English Exam", R.drawable.pastpapers));
        factories.add(new TableBean("Mathematics Exam", R.drawable.pastpapers));
        factories.add(new TableBean("Physics", R.drawable.pastpapers));
        factories.add(new TableBean("Geography", R.drawable.pastpapers));
        return factories ;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EExamActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {
        //pass intent to activity here
        //in the meantime we will get position of clicked item
        selectedExam = examsIndexes.get(position);
        Intent intent = new Intent(this, StartNewExamActivity.class);
        startActivity(intent);
}

}
