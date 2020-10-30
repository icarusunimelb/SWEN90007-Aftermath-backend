package controllers;

import DTO.DTOSubjectSubmission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.google.gson.Gson;
import datamapper.InstructorMapper;
import datamapper.StudentMapper;

import domain.Subject;
import exceptions.RecordNotExistException;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.CrossOrigin;
import security.TokenVerification;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@WebServlet("/api/exams-controller")
public class examsController extends HttpServlet {
    private static final long serialVersionUID = 3L;

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
        try {
            String status = request.getParameter("status");
            //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            String token = TokenVerification.getTokenFromHeader(request);
            String userIdAndUserType = TokenVerification.getIdAndSubject(token);
            String userId = userIdAndUserType.split(",", 2)[0];
//        String userType = userIdAndUserType.split(",", 2)[1];

            if(status.equals("managing")){
                String jsonArray = manageExams(userId);
                String newJsonArray = jsonArray.replace("\"id\":", "\"dataId\":");

                out.print(newJsonArray);
                //System.out.println(newJsonArray);
                //System.out.println("this is printing JSONArray!!!!!!!!!!!!!!!!!!!");
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();
                return;
            } else if(status.equals("marking")){
                String jsonArray = markExams(userId);
                String newJsonArray = jsonArray.replace("\"id\":", "\"dataId\":")
                        .replace("\"examAnswers\":", "\"submissions\":");
                out.print(newJsonArray);
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();
                return;
            } else if(status.equals("taking")){
                // get exams of a student who can take right now
                String jsonArray =  takeExams(userId);
                String newJsonArray = jsonArray.replace("\"id\":", "\"dataId\":");
                out.print(newJsonArray);
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();
                return;
            } else {
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
                out.print(jsonObject);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
        //System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
    }

    private String manageExams(String userId){
        List<Subject> subjects = InstructorMapper.getSingletonInstance().getManagingSubjects(userId);

        String json = new Gson().toJson(subjects);
        String newJsonArray = json.replace("\"id\":", "\"dataId\":");
//        JSONArray jsonArray = new JSONArray(subjects);

        return newJsonArray;
    }

    private String markExams(String userId) throws RecordNotExistException {
        List<Subject> subjects = InstructorMapper.getSingletonInstance().getMarkingSubjects(userId);
        List<DTOSubjectSubmission> dtoSubjectSubmissions = new ArrayList<>();

        if (subjects.size()>0) {
            //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            dtoSubjectSubmissions = subjects.stream().map(DTOSubjectSubmission::new)
                    .collect(Collectors.toList());
        }else {
            //System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb");
        }

        String json = new Gson().toJson(dtoSubjectSubmissions);
//        JSONArray jsonArray = new JSONArray(jsonString);

        return json;
    }

    private String takeExams(String userId) throws RecordNotExistException {
        List<Subject> subjects = StudentMapper.getSingletonInstance().getTakingSubjects(userId);

        String json = new Gson().toJson(subjects);
        String newJsonArray = json.replace("\"id\":", "\"dataId\":");

//        JSONArray jsonArray = new JSONArray(jsonString);

        return newJsonArray;
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization");
    }
}

