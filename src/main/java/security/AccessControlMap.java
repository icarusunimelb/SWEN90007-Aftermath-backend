package security;

import utils.USERTYPE;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccessControlMap {
    static final Map<USERTYPE, String> ACCESS_CONTROL;

    static {
        ACCESS_CONTROL = new LinkedHashMap<>();
        ACCESS_CONTROL.put(USERTYPE.ADMIN, "/api/user/login,/api/user/logout," +
                "/api/subject-controller,/api/subjects-controller,/api/users-controller");
        ACCESS_CONTROL.put(USERTYPE.LECTURER, "/api/user/login,/api/user/logout,/api/exam-controller," +
                "/api/exams-controller,/api/examAnswer-controller,/api/question-controller");
        ACCESS_CONTROL.put(USERTYPE.STUDENT, "/api/user/login,/api/user/logout,/api/exams-controller," +
                "/api/examAnswer-controller,/api/question-controller");
    }
}
