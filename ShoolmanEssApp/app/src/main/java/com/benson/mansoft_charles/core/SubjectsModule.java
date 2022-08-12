package com.benson.mansoft_charles.core;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.StudentSubjectsBean;
import com.benson.mansoft_charles.beans.TeachersBean;
import com.benson.mansoft_charles.enums.SchoolTasks;
import com.benson.mansoft_charles.interfaces.SubjectsInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by mansoft-charles on 6/22/18.
 */

public class SubjectsModule implements SubjectsInterface {
    @Override
    public void loadStudentSubjects(final AppCompatActivity context) throws JSONException {
        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/getStudentSubjectsSum/" + ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.termSession.getTermId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            ESSStaticVariables.CAN_GO_BACK = true;
                            context.setContentView(R.layout.student_subject);

                            String data [][] = getStudentSubjects(new JSONObject(response));

                            final String[][] spaceProbes= data;
                            final String[] spaceProbeHeaders={"SUBJECT","MARK","GRADE"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewSubjects);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(3);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {
                                    final int index = rowIndex;
                                    ESSStaticVariables.CAN_GO_BACK = true;
                                    ESSStaticVariables.selectedTask = SchoolTasks.SUBJECTS;
                                    StudentSubjectsBean obj = ESSStaticVariables.studentSubjects.get(index);
                                    context.setContentView(R.layout.student_subject_more_details);

                                    EditText subCode = (EditText) context.findViewById(R.id.subjectCode);
                                    subCode.setText(obj.getSubjectShortCode());

                                    EditText subName = (EditText) context.findViewById(R.id.subjectName);
                                    subName.setText(obj.getSubjectName());

                                    EditText mark = (EditText) context.findViewById(R.id.mark);
                                    mark.setText(obj.getThisTermMark().toString());

                                    EditText targetMark = (EditText) context.findViewById(R.id.targetMark);
                                    targetMark.setText(obj.getTargetMark().toString());


                                    EditText grade = (EditText) context.findViewById(R.id.grade);
                                    grade.setText(obj.getThisTermGrade());

                                    try {
                                        loadClassSubjects(context, obj.getSubjectId());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                            tableView.addHeaderClickListener(new TableHeaderClickListener() {
                                @Override
                                public void onHeaderClicked(int columnIndex) {

                                }
                            });

                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
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
        queue.add(stringRequest);

    }

    @Override
    public String[][] getStudentSubjects(JSONObject value) throws JSONException {
        JSONObject response = value.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length()][3];
        ESSStaticVariables.studentSubjects = new ArrayList<StudentSubjectsBean>();
        for(int i =0;i<data.length();i++)
        {
            JSONObject obj = data.getJSONObject(i);
            responseObj[i][0] = obj.getString("subjectName");
            responseObj[i][1] = String.valueOf(obj.getDouble("thisTermMark"));
            responseObj[i][2] = obj.getString("thisTermGrade");
            ESSStaticVariables.studentSubjects.add(new StudentSubjectsBean(
                    obj.getString("subjectid"),
                    obj.getString("subjectShortCode"),
                    obj.getString("subjectName"),
                    obj.getString("lastTermGrade"),
                    obj.getDouble("lastTermMark"),
                    obj.getDouble("thisTermMark"),
                    obj.getString("thisTermGrade"),
                    obj.getDouble("thisTermTarget")
            ));
        }

        return responseObj;
    }

    @Override
    public void loadClassSubjects(final AppCompatActivity context, final String subjectId) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/listAllSubjectsSM/" + ESSStaticVariables.session.getTenantId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            JSONObject res = new JSONObject(response).getJSONObject("response");
                            JSONArray data = res.getJSONArray("data");
                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject obj = data.getJSONObject(i);
                                String subject = obj.getString("mansoftltdSchsubId").trim();
                                if(subject.equals(subjectId))
                                {
                                    loadClassSubjectTeachers(context, subjectId);
                                }
                            }


                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void loadClassSubjectTeachers(final AppCompatActivity context, String subjectId) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/listClassSubjectTeachers/" +
                ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.studentClass +"/" + subjectId +"/" +
                ESSStaticVariables.termSession.getTermId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            JSONObject res = new JSONObject(response).getJSONObject("response");
                            JSONArray data = res.getJSONArray("data");
                            for(int i=0;i<data.length();i++)
                            {
                                if(i==0) {
                                    JSONObject obj = data.getJSONObject(i);
                                    final TeachersBean teacherObj = getTeacherDetails(context, obj.getString("mansoftltdSchTeacherId"));
                                    EditText teacher = (EditText) context.findViewById(R.id.teacher);
                                    teacher.setText(teacherObj.getName());

                                    final EditText mobile = (EditText) context.findViewById(R.id.mobile);
                                    mobile.setText(teacherObj.getMobile());

                                    mobile.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {
                                            String number = mobile.getText().toString();
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:"+number));
                                            context.startActivity(intent);
                                            return true;
                                        }
                                    });

                                    final EditText email = (EditText) context.findViewById(R.id.email);
                                    email.setText(teacherObj.getEmail());
                                    email.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {
                                            try {
                                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                                emailIntent.putExtra(Intent.EXTRA_EMAIL, teacherObj.getEmail());
                                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                                emailIntent.setType("text/plain");
                                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                                                final PackageManager pm = context.getPackageManager();
                                                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                                                ResolveInfo best = null;
                                                for (final ResolveInfo info : matches)
                                                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                                        best = info;
                                                if (best != null)
                                                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                                                context.startActivity(emailIntent);
                                                return true;
                                            }
                                            catch(Exception e)
                                            {
                                                Toast.makeText(context.getApplicationContext(), e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                                                return false;
                                            }
                                        }
                                    });
                                }

                            }


                        }
                        catch(JSONException ee) {
                            Toast.makeText(context.getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public TeachersBean getTeacherDetails(AppCompatActivity context, String teacher) {
        TeachersBean response = null;
        for(TeachersBean e : ESSStaticVariables.schoolTeachers)
        {
            if(e.getUserId().equals(teacher))
            {
                response = e;
                break;
            }
        }
        return response;
    }
}
