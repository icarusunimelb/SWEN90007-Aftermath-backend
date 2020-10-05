package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import datamapper.InstructorMapper;
import datamapper.StudentMapper;

import domain.Subject;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.TokenVerification;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/exams-controller")
public class examsController extends HttpServlet {
    private static final long serialVersionUID = 2L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public examsController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */ // /api/exam-controller?status=managing
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String status = request.getParameter("status");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        String token = TokenVerification.getTokenFromHeader(request);
        String userIdAndUserType = "";
        System.out.println("token: " + token);
        if(token.equals("")){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        } else {
            userIdAndUserType = TokenVerification.verifyToken(token);
            System.out.println("userId and User Type");
            if(userIdAndUserType.equals("")){
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
                out.print(jsonObject);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.flush();
                return;
            }

        }


        String userId = userIdAndUserType.split(",", 2)[0];
//        String userType = userIdAndUserType.split(",", 2)[1];

        if(status.equals("managing")){
            JSONArray jsonArray = manageExams(userId);
            out.print(jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else if(status.equals("marking")){
            JSONArray jsonArray = markExams(userId);
            out.print(jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else if(status.equals("taking")){
            // get exams of a student who can take right now
            JSONArray jsonArray =  takeExams(userId);
            out.print(jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else {
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String subjectId = request.getParameter("subject_id");
//        String examName = request.getParameter("examName");
//
//        Exam exam = new Exam();
//        exam.setSubjectID(subjectId);
//        exam.setExamName(examName);
//        exam.setId(KeyGenerator.getSingletonInstance().getKey(exam));
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerNew(exam);
//        UnitOfWork.getCurrent().commit();
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String examID =request.getParameter("exam_id");
//        String subjectId = request.getParameter("subject_id");
//        String examName = request.getParameter("examName");
//
//        Exam exam = new Exam();
//        exam.setSubjectID(subjectId);
//        exam.setExamName(examName);
//        exam.setId(examID);
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerDirty(exam);
//        UnitOfWork.getCurrent().commit();
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String examID =request.getParameter("dataId");
//
//        Exam exam = new Exam();
//        exam.setId(examID);
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerDeleted(exam);
//        UnitOfWork.getCurrent().commit();
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();

    }

    private JSONArray manageExams(String userId){
        List<Subject> subjects = InstructorMapper.getSingletonInstance().getManagingSubjects(userId);

        String jsonString = convertToJson(subjects);

        JSONArray jsonArray = new JSONArray(jsonString);

        return jsonArray;
    }

    private JSONArray markExams(String userId){
        List<Subject> subjects = InstructorMapper.getSingletonInstance().getMarkingSubjects(userId);

        String jsonString = convertToJson(subjects);

        JSONArray jsonArray = new JSONArray(jsonString);

        return jsonArray;
    }

    private JSONArray takeExams(String userId){
        List<Subject> subjects = StudentMapper.getSingletonInstance().getTakingSubjects(userId);
        String jsonString = convertToJson(subjects);

        JSONArray jsonArray = new JSONArray(jsonString);

        return jsonArray;
    }

    private String convertToJson(List<Subject> subjects){
        ObjectMapper mapper = new ObjectMapper();
        String subjectsJsonString = "[";
        for(Subject subject: subjects){
            try {
                String jsonString = mapper.writeValueAsString(subject);
//                System.out.println("jsonString!!! " + jsonString);
                subjectsJsonString = subjectsJsonString + jsonString;
                subjectsJsonString = subjectsJsonString + ",";
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        subjectsJsonString = subjectsJsonString.substring(0,subjectsJsonString.length()-1);
        subjectsJsonString = subjectsJsonString + "]";
        if(subjectsJsonString.length()<2){
            subjectsJsonString = "[]";
        }
        return subjectsJsonString;
    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}

