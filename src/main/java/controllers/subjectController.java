package controllers;

import domain.Subject;
import domain.SubjectInstructorMap;
import domain.SubjectStudentMap;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.KeyGenerator;
import security.TokenVerification;
import utils.UnitOfWork;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet("/api/subject-controller")
public class subjectController extends HttpServlet {
    private static final long serialVersionUID = 7L;

    // add one subject
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {

            // TODO save a new subject
            String requestData = request.getReader().lines().collect(Collectors.joining());
            JSONObject jsonObject = new JSONObject(requestData);

            String subjectCode = jsonObject.getString("subjectId");
            String subjectName = jsonObject.getString("subjectName");
            JSONArray lecturers = jsonObject.getJSONArray("lecturers");
            JSONArray students = jsonObject.getJSONArray("students");

            UnitOfWork.newCurrent();

            Subject subject = new Subject();
            String key = KeyGenerator.getSingletonInstance().getKey(subject);
            subject.setId(key);
            subject.setSubjectCode(subjectCode);
            subject.setSubjectName(subjectName);
            UnitOfWork.getCurrent().registerNew(subject);
            for (int i = 0; i < lecturers.length(); i++) {
                SubjectInstructorMap subjectInstructorMap = new SubjectInstructorMap();
                subjectInstructorMap.setInstructorID(lecturers.getString(i));
                subjectInstructorMap.setSubjectID(key);
                UnitOfWork.getCurrent().registerNew(subjectInstructorMap);
            }
            for (int i = 0; i < students.length(); i++) {
                SubjectStudentMap subjectStudentMap = new SubjectStudentMap();
                subjectStudentMap.setStudentID(students.getString(i));
                subjectStudentMap.setSubjectID(key);
                UnitOfWork.getCurrent().registerNew(subjectStudentMap);
            }

            UnitOfWork.getCurrent().commit();

            JSONObject jsonObject1 = new JSONObject(String.format(
                    "{\"code\":\"%s\",\"subjectId\":\"%s\"}", HttpServletResponse.SC_OK, subject.getId()));
            out.print(jsonObject1);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\",\"subjectId\":\"%s\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
        }
    }

}
