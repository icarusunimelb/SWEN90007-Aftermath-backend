package controllers;

import DTO.DTOSubject;
import com.google.gson.Gson;
import datamapper.SubjectMapper;
import domain.Subject;
import org.json.JSONObject;
import security.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/subjects-controller") @MultipartConfig
public class subjectsController extends HttpServlet {
    private static final long serialVersionUID = 8L;
    // get all subjects
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try{

            // TODO get all subjects, corresponding instructors, students, exams
            List<Subject> subjects = SubjectMapper.getSingletonInstance().getAllSubjects();
            List<DTOSubject> dtoSubjects = new ArrayList<>();
            for (Subject subject : subjects){
                dtoSubjects.add(new DTOSubject(subject));
            }

            String json = new Gson().toJson(dtoSubjects);
            out.print(json);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();

        } catch (Exception e){
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\",\"subjectId\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization");
    }
}
