package com.benson.mansoft_charles.core;

/**
 * Created by mansoft-charles on 5/3/18.
 */


import com.benson.mansoft_charles.beans.AssignmentsBean;
import com.benson.mansoft_charles.beans.PastPapersBean;
import com.benson.mansoft_charles.beans.StudentDisciplineBean;
import com.benson.mansoft_charles.beans.StudentFeesBean;
import com.benson.mansoft_charles.beans.StudentSubjectsBean;
import com.benson.mansoft_charles.beans.TeachersBean;
import com.benson.mansoft_charles.beans.TermDetailsBean;
import com.benson.mansoft_charles.enums.SchoolTasks;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESSStaticVariables {

    public static boolean CAN_GO_BACK = false;

    public static String userDisplayHeader = "";

    public static String BACKEND_ROOT_URL;

    public static String BACKEND_SCHOOL_URL;

    public static String LOGIN_URL = "adminrs/core/auth/ess";

    public static String USER_CONFIG = "user_config";

    public static String SERVER_CONFIG = "server_config";

    public static String GLOBAL_PATH;

    public static String PAYROLL_URL;

    public static LoginCoreParameters session = new LoginCoreParameters();

    public static List<StudentFeesBean> studentFeesBean;

    public static TermDetailsBean termSession;

    public static List<TermDetailsBean> studentTermDetails;

    public static List<AssignmentsBean> assignments;

    public static List<StudentSubjectsBean> studentSubjects;

    public static List<TeachersBean> schoolTeachers;

    public static String studentClass;

    public static SchoolTasks selectedTask;

    public static HashMap<String, String> studentAwards;

    public static HashMap<String, String> disciplineActions;

    public static List<StudentDisciplineBean> studentDiscipline;

    public static List<PastPapersBean> studentPastPapers;

    public static Boolean showAuthentication;

    public static Integer examTimeInSeconds;

    public static Integer questionNo;

    public static String examSession;

    public static JSONObject currentQuestion;

    public static JSONObject previousQuestion;

    public static DSAction<JSONObject, String> examAction;

    public static String examDescription;

    public static Map<String, Boolean> startedExams = new HashMap<>();


}
