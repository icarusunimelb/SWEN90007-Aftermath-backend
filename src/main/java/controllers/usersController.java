package controllers;

import DTO.DTOInstructor;
import DTO.DTOStudent;
import DTO.DTOUsers;
import com.google.gson.Gson;
import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.Instructor;
import domain.Student;
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

@WebServlet("/api/users-controller") @MultipartConfig
public class usersController extends HttpServlet {
    private static final long serialVersionUID = 9L;
    // get all users
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try{

            // TODO get all users
            List<Instructor> allInstructors = InstructorMapper.getSingletonInstance().getAllInstructors();
            List<DTOInstructor> allDTOInstructors = new ArrayList<>();
            List<Student> allStudents = StudentMapper.getSingletonInstance().getAllStudents();
            List<DTOStudent> allDTOStudents = new ArrayList<>();
            for (Instructor instructor : allInstructors){
                allDTOInstructors.add(new DTOInstructor(instructor));
            }
            for (Student student : allStudents){
                allDTOStudents.add(new DTOStudent(student));
            }
            DTOUsers dtoUsers = new DTOUsers(allDTOInstructors, allDTOStudents);

            String json = new Gson().toJson(dtoUsers);
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
}
