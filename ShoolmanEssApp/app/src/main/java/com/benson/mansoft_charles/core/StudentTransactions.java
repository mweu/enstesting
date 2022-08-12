package com.benson.mansoft_charles.core;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.PastPapersBean;
import com.benson.mansoft_charles.beans.StudentDisciplineBean;
import com.benson.mansoft_charles.enums.SchoolTasks;
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
 * Created by mansoft-charles on 6/25/18.
 */

public class StudentTransactions implements com.benson.mansoft_charles.interfaces.StudentTransactions {
    @Override
    public void showStudentAwards(final AppCompatActivity context) throws JSONException {
        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/getStudentAwards/" + ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.session.getTenantId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            context.setContentView(R.layout.awards);
                            ESSStaticVariables.CAN_GO_BACK = true;
                            final String[][] spaceProbes= getStudentAwards(new JSONObject(response));
                            final String[] spaceProbeHeaders={"Award","Date"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewAwards);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(2);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {

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
    public String[][] getStudentAwards(JSONObject value) throws JSONException {
       JSONObject response = value.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length()][2];
        for(int i=0;i<data.length();i++)
        {
            JSONObject obj = data.getJSONObject(i);
            responseObj[i][0] = ESSStaticVariables.studentAwards.get(obj.getString("award"));
            responseObj[i][1] = obj.getString("date");
        }
        return responseObj;
    }

    @Override
    public void showStudentDiscipline(final AppCompatActivity context) throws JSONException {
        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/getUserDisciplinary/" + ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.session.getTenantId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            context.setContentView(R.layout.discipline);
                            ESSStaticVariables.CAN_GO_BACK = true;
                            final String[][] spaceProbes= getStudentDiscipline(new JSONObject(response), context);
                            final String[] spaceProbeHeaders={"Author","Date"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewDiscipline);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(2);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {
                                    context.setContentView(R.layout.discipline_more_actions);
                                    ESSStaticVariables.CAN_GO_BACK = true;
                                    ESSStaticVariables.selectedTask = SchoolTasks.DISCIPLINE;
                                    StudentDisciplineBean obj = ESSStaticVariables.studentDiscipline.get(rowIndex);

                                    EditText date = (EditText) context.findViewById(R.id.disciplineDate);
                                    date.setText(obj.getDate());

                                    EditText author = (EditText) context.findViewById(R.id.disciplineAuthor);
                                    author.setText(obj.getAuthor());

                                    EditText comments = (EditText) context.findViewById(R.id.disciplineComments);
                                    comments.setText(obj.getComments());

                                    EditText action = (EditText) context.findViewById(R.id.disciplineaction);
                                    action.setText(obj.getAction());

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
    public String[][] getStudentDiscipline(JSONObject value, final AppCompatActivity context) throws JSONException {
        JSONObject response = value.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length()][2];
        ESSStaticVariables.studentDiscipline = new ArrayList<StudentDisciplineBean>();
        for(int i=0;i<data.length();i++)
        {
            JSONObject obj = data.getJSONObject(i);
            String author = new SubjectsModule().getTeacherDetails(context, obj.getString("author")).getName();
            responseObj[i][0] = author;
            responseObj[i][1] = obj.getString("date");

            ESSStaticVariables.studentDiscipline.add(new StudentDisciplineBean(
                    obj.getString("comments"),
                    ESSStaticVariables.disciplineActions.get(obj.getString("action")),
                    obj.getString("date"),
                    author
            ));
        }
        return responseObj;
    }

    @Override
    public void showPastPapers(final AppCompatActivity context) throws JSONException {
        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/fetchStudentPastPapares/" +
                ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.termSession.getTermId() +"/" + ESSStaticVariables.session.getTenantId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            context.setContentView(R.layout.pastpapers);
                            ESSStaticVariables.CAN_GO_BACK = true;
                            final String[][] spaceProbes= getStudentPastPapers(new JSONObject(response), context);
                            final String[] spaceProbeHeaders={"Subject","Title"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewPastPapers);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(2);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {
                                    try {
                                        ESSStaticVariables.CAN_GO_BACK = true;
                                        ESSStaticVariables.selectedTask = SchoolTasks.PAST_PAPERS;
                                        new Reports().openUrl(context,
                                                ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/openPastPaper/" +
                                                ESSStaticVariables.studentPastPapers.get(rowIndex).getIndex());
                                    } catch (Exception e) {
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
    public String[][] getStudentPastPapers(JSONObject value, AppCompatActivity context) throws JSONException {
        JSONObject response = value.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length()][2];
        ESSStaticVariables.studentPastPapers = new ArrayList<PastPapersBean>();
        for(int i=0;i<data.length();i++)
        {
            JSONObject obj = data.getJSONObject(i);
            responseObj[i][0] = obj.getString("subjectName");
            responseObj[i][1] = obj.getString("title");

            ESSStaticVariables.studentPastPapers.add(new PastPapersBean(
                    obj.getString("index"),
                    obj.getString("title"),
                    obj.getString("description")));

        }
        return responseObj;

    }

    @Override
    public void loadSharing(AppCompatActivity context) throws Exception {
        ESSStaticVariables.CAN_GO_BACK = true;
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
           /* emailIntent.putExtra(Intent.EXTRA_EMAIL, "mansoft@gmail.com");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");*/
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "We use SCHOOLMAN for our Financials & Academics. \n " +
                            "View; https://www.mansoftweb.com/mobile/schoolman.pdf \n" +
                             "Visit; http://www.mansoftweb.com");
            final PackageManager pm = context.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            /*for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                {
                    best = info;
                }
            if (best != null)
            {
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            }*/
            context.startActivity(emailIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(context.getApplicationContext(), e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void performLogOutTask(
            final AppCompatActivity context,
            final String title,
            final String message)
    {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        new FilesMan().writeFile(
                                ""+","+"" +"," + "",
                                ESSStaticVariables.USER_CONFIG,
                                context);
                        ESSStaticVariables.session = new LoginCoreParameters();
                        System.exit(0);
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
}
