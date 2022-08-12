package com.benson.mansoft_charles.core;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.AssignmentsBean;
import com.benson.mansoft_charles.interfaces.AssignmentsInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
import com.benson.mansoft_charles.enums.SchoolTasks;
import com.benson.mansoft_charles.shoolmanportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by mansoft-charles on 6/21/18.
 */

public class Assignments implements AssignmentsInterface {
    @Override
    public void loadAssignments( final AppCompatActivity context) throws Exception, JSONException {
        MainActivity.pd.setMessage("Please waite..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/fetchStudentPosts/" + ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.termSession.getTermId() +"/" + ESSStaticVariables.session.getTenantId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {

                            ESSStaticVariables.CAN_GO_BACK = true;
                            context.setContentView(R.layout.student_fees);
                            final String[][] spaceProbes= getStudentAssignments(new JSONObject(response));
                            final String[] spaceProbeHeaders={"SUBJECT","TITLE","DATE"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewFees);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(3);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {

                                    String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/openAssignment/" +
                                            ESSStaticVariables.assignments.get(rowIndex).getIndex();
                                    try {
                                        ESSStaticVariables.CAN_GO_BACK = true;
                                        ESSStaticVariables.selectedTask = SchoolTasks.ASSIGNMENTS;

                                        new Reports().openUrl(context, url);
                                    } catch (Exception e) {
                                        Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    public String[][] getStudentAssignments(JSONObject values) throws JSONException
    {

        JSONObject response = values.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseArray = new String[data.length()][3];
        ESSStaticVariables.assignments = new ArrayList<AssignmentsBean>();
        for(int i=0;i<data.length(); i++)
        {
            JSONObject obj = data.getJSONObject(i);
            responseArray[i][0] = obj.getString("subjectName");
            responseArray[i][1] = obj.getString("title");
            responseArray[i][2] = obj.getString("date");
            ESSStaticVariables.assignments.add(new AssignmentsBean(
                    obj.getString("index"),
                    obj.getString("subjectuuid"),
                    obj.getString("subjectName"),
                    obj.getString("title"),
                    obj.getString("description"),
                    obj.getString("teacheruuid"),
                    obj.getString("teacherName"),
                    obj.getString("date")
            ));
        }
        return responseArray;
    }

}
