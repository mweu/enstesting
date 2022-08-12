package com.benson.mansoft_charles.core;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.StudentFeesBean;
import com.benson.mansoft_charles.enums.SchoolTasks;
import com.benson.mansoft_charles.interfaces.FeesInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;

import com.benson.mansoft_charles.shoolmanportal.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by mansoft-charles on 6/19/18.
 */

public class FeesModule implements FeesInterface {
    @Override
    public void loadFeesInterface(final AppCompatActivity context) throws JSONException {

        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/getStudentFeeStatus/" + ESSStaticVariables.session.getUserId() +"/"+
                ESSStaticVariables.session.getTenantId() +"/" +ESSStaticVariables.termSession.getTermId() +"/" +
                ESSStaticVariables.termSession.getTermSession();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( MainActivity.pd.isShowing()) {
                            MainActivity.pd.dismiss();
                        }
                        try {
                            context.setContentView(R.layout.student_fees);
                            ESSStaticVariables.CAN_GO_BACK = true;
                            final String[][] spaceProbes= getStudentFeeData(new JSONObject(response), context);
                            final String[] spaceProbeHeaders={"Account","Balance"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewFees);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(2);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));

                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {
                                    ESSStaticVariables.CAN_GO_BACK = true;
                                    ESSStaticVariables.selectedTask = SchoolTasks.FEES;
                                    context.setContentView(R.layout.student_fees_details);

                                    StudentFeesBean bean = ESSStaticVariables.studentFeesBean.get(rowIndex);
                                    EditText bf = (EditText) context.findViewById(R.id.balanceBf);
                                    bf.setText(bean.getBalanceBf().toString());

                                    EditText outstating = (EditText) context.findViewById(R.id.outstanding);
                                    outstating.setText(bean.getOutstating().toString());

                                    EditText total = (EditText) context.findViewById(R.id.total);
                                    total.setText(bean.getTotal().toString());

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
    public String[][] getStudentFeeData(JSONObject values, AppCompatActivity context) throws JSONException {
        ESSStaticVariables.studentFeesBean = new ArrayList<StudentFeesBean>();
       JSONObject response = values.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length() + 1][2];
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        double total = 0;
        for(int i=0;i<data.length();i++)
        {
            responseObj[i][0] = data.getJSONObject(i).getString("accountName");
            responseObj[i][1] = formatter.format(data.getJSONObject(i).getDouble("totalBalance"));
            ESSStaticVariables.studentFeesBean.add(new StudentFeesBean(
                    data.getJSONObject(i).getString("accountCode"),
                    data.getJSONObject(i).getString("accountName"),
                    data.getJSONObject(i).getDouble("balanceBf"),
                    data.getJSONObject(i).getDouble("amounttoPay"),
                    data.getJSONObject(i).getDouble("totalBalance")
            ));
            total = total + data.getJSONObject(i).getDouble("totalBalance");
        }
        responseObj[data.length()][0] = "TOTAL";
        responseObj[data.length()][1] = String.valueOf(formatter.format(total));
        return responseObj;
    }
}
