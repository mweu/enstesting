package com.benson.mansoft_charles.core;

import android.support.v7.app.AppCompatActivity;

import com.benson.mansoft_charles.enums.SchoolTasks;
import com.benson.mansoft_charles.interfaces.EventsSwitchInterface;
import com.benson.mansoft_charles.shoolmanportal.MainActivity;

import org.json.JSONException;

/**
 * Created by mansoft-charles on 6/22/18.
 */

public class EventsSwitch implements EventsSwitchInterface{

    @Override
    public void swichSchoolIntent(AppCompatActivity context, SchoolTasks tasks) {
        switch(tasks)
        {
            case FEES:
                try {
                    new FeesModule().loadFeesInterface(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case REPORTS:
                try {
                    new Reports().loadTermDetails(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case ASSIGNMENTS:
                try {
                    new Assignments().loadAssignments(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case SUBJECTS:
                try {
                    new SubjectsModule().loadStudentSubjects(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case DISCIPLINE:
                try {
                    new StudentTransactions().showStudentDiscipline(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case PAST_PAPERS:
                try {
                    new StudentTransactions().showPastPapers(context);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                try {
                    new MainActivity().initiateVariables(context);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
}
