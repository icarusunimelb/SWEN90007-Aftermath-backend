package controllers;

import com.google.gson.JsonObject;
import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.Instructor;
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
import java.util.Enumeration;
import org.apache.commons.io.IOUtils;
import  org.json.*;
import com.google.gson.Gson;

@WebServlet("/user-logout")
public class logoutController extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public logoutController() {
        super();
    }
    private enum USERTYPE {STUDENT, INSTRUCTOR,ADMIN};
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {

            String headerName = headerNames.nextElement();
            if(headerName.equals("token")){
                Enumeration<String> headers = request.getHeaders(headerName);
                while (headers.hasMoreElements()) {

                    String headerValue = headers.nextElement();

                    TokenVerification.removeToken(headerValue);
                        JSONObject jsonObject = new JSONObject(String.format(
                                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
                    out.print(jsonObject);
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.flush();
                    return;

                }
            }
        }
        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.flush();
    }



}
