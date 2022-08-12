package com.benson.mansoft_charles.core;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.benson.mansoft_charles.beans.TermDetailsBean;
import com.benson.mansoft_charles.enums.SchoolTasks;
import com.benson.mansoft_charles.interfaces.ReportsInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;
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
 * Created by mansoft-charles on 6/20/18.
 */

public class Reports implements ReportsInterface {
    @Override
    public void loadTermDetails(final AppCompatActivity context) throws JSONException, Exception {
        MainActivity.pd.setMessage("Please wait ..");
        MainActivity.pd.setCancelable(false);
        MainActivity.pd.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ESSStaticVariables.BACKEND_SCHOOL_URL +"/school/getSchoolTerms/" + ESSStaticVariables.session.getTenantId() +"/"+
                ESSStaticVariables.session.getUserId();
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
                            final String[][] spaceProbes= getTermDetails(context, response);
                            final String[] spaceProbeHeaders={"Term"};
                            final TableView<String[]> tableView = (TableView<String[]>) context.findViewById(R.id.tableViewFees);
                            tableView.setHeaderBackgroundColor(Color.parseColor("#3f51b5"));
                            tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context,spaceProbeHeaders));
                            tableView.setColumnCount(1);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(context, spaceProbes));
                            tableView.addDataClickListener(new TableDataClickListener() {
                                @Override
                                public void onDataClicked(int rowIndex, Object clickedData) {
                                    final int index = rowIndex;
                                    ESSStaticVariables.CAN_GO_BACK = true;
                                    ESSStaticVariables.selectedTask = SchoolTasks.REPORTS;
                                    context.setContentView(R.layout.student_reports_select);
                                    Button reportForm = (Button) context.findViewById(R.id.reportForm);
                                    reportForm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                openUrl(context, ESSStaticVariables.BACKEND_SCHOOL_URL +"/schoolReports/generateReportForm/" +
                                                ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.session.getUserId() +"/" +
                                                ESSStaticVariables.studentTermDetails.get(index).getTermId() +"/" +
                                                                ESSStaticVariables.studentTermDetails.get(index).getTermSession()
                                                );
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    Button transactions = (Button) context.findViewById(R.id.feeTransactions);
                                    transactions.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                openUrl(context, ESSStaticVariables.BACKEND_SCHOOL_URL +"/schoolReports/getStudentStatements/" +
                                                        ESSStaticVariables.session.getTenantId() +"/" + ESSStaticVariables.session.getUserId() +"/" +
                                                        ESSStaticVariables.studentTermDetails.get(index).getTermId() +"/" +
                                                        ESSStaticVariables.studentTermDetails.get(index).getTermSession());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

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
    public String[][] getTermDetails(AppCompatActivity context, String values) throws JSONException, Exception {
        ESSStaticVariables.studentTermDetails = new ArrayList<TermDetailsBean>();
        JSONObject response = new JSONObject(values).getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        String[][] responseObj = new String[data.length()][1];
        for(int i=0;i<data.length();i++)
        {
            JSONObject obj = data.getJSONObject(i);
            responseObj[i][0] = obj.getString("termDesc");
            ESSStaticVariables.studentTermDetails.add(new TermDetailsBean(
                    obj.getString("termId"),
                    obj.getString("termDesc"),
                    obj.getString("sessionId")));
        }
        return responseObj;
    }

    @Override
    public void openUrl(final AppCompatActivity context, final String url) throws Exception {

        context.setContentView(R.layout.url_opener);
        ESSStaticVariables.CAN_GO_BACK = true;
        WebView webView = (WebView) context.findViewById(R.id.url_opener);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if(MainActivity.pd == null) {
                    MainActivity.pd = new ProgressDialog(context);
                    // in standard case YourActivity.this

                    MainActivity.pd.setMessage("Loading...");
                    MainActivity.pd.show();
                }

            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (MainActivity.pd.isShowing()) {
                        MainActivity.pd.dismiss();
                        MainActivity.pd = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://docs.google.com/viewer?url=" +url);
    }
}
