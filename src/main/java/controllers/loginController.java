package controllers;

import datamapper.AdminMapper;
import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.User;
import security.TokenVerification;

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
import utils.USERTYPE;

@WebServlet("/login")
public class loginController extends HttpServlet {
    private static final long serialVersionUID = 4L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginController() {
        super();
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);

            String email = user.getEmail();
            String password = user.getPassword();
//            System.out.println(email+password);
            PrintWriter out = response.getWriter();

            String userType = null;
            String dataID = null;
            String token = null;
            String instructorID = InstructorMapper.getSingletonInstance().authenticate(email, password);
            String studentID = StudentMapper.getSingletonInstance().authenticate(email, password);

            String adminID = AdminMapper.getSingletonInstance().authenticate(email, password);

            if (!instructorID.isEmpty()) {
                userType = USERTYPE.LECTURER.toString();
                dataID = instructorID;
            } else if (!studentID.isEmpty()) {
                userType = USERTYPE.STUDENT.toString();
                dataID = studentID;
            } else if (!adminID.isEmpty()){
                userType = USERTYPE.ADMIN.toString();
                dataID = adminID;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}", HttpServletResponse.SC_UNAUTHORIZED));
                out.print(jsonObject);
                out.flush();
                return;
            }
            System.out.println(dataID + userType);
            token = TokenVerification.createJWT(dataID, userType);
            System.out.println("this is ur token: "+ token);
            //System.out.println(dataID+password+token);
            try {
                TokenVerification.addToken(token);
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\",\"dataId\":\"%s\",\"userType\":\"%s\",\"token\":\"%s\"}", HttpServletResponse.SC_OK, dataID, userType, token));
                out.print(jsonObject);
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();

            } catch (JSONException err) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
                out.print(jsonObject);
                out.flush();
            }
        }catch (Exception e){
            System.out.println(this.getClass()+e.getMessage());
        }

    }

}
