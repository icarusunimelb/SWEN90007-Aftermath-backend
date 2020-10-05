package controllers;

import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.User;
import utils.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import  org.json.*;
import com.google.gson.Gson;

@WebServlet("/api/user/login")
public class loginController extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginController() {
        super();
    }
    private enum USERTYPE {STUDENT, LECTURER,ADMIN};
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        User user = gson.fromJson(reader, User.class);

        String email = user.getEmail();
        String password = user.getPassword();
        System.out.println(email+password);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        String userType = null;
        String dataID = null;
        String token = null;
        String instructorID = InstructorMapper.getSingletonInstance().authenticate(email, password);
        String studentID = StudentMapper.getSingletonInstance().authenticate(email,password);
        if (!instructorID.isEmpty()) {
            userType = USERTYPE.LECTURER.toString();
            dataID = instructorID;
        }else if (!studentID.isEmpty()){
            userType = USERTYPE.STUDENT.toString();
            dataID = studentID;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            out.flush();
            return;
        }
        token = TokenVerification.createJWT(dataID, userType);
        System.out.println(dataID+password+token);
        try {
            TokenVerification.addToken(token);
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\",\"dataId\":\"%s\",\"userType\":\"%s\",\"token\":\"%s\"}",HttpServletResponse.SC_OK,dataID,userType, token));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();

        }catch (JSONException err){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            out.flush();
        }

    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

}
