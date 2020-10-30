package controllers;

import security.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import  org.json.*;
import utils.LockManager;

@WebServlet("/api/user/logout")
public class logoutController extends HttpServlet {
    private static final long serialVersionUID = 5L;
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
        try{
            String token = TokenVerification.getTokenFromHeader(request);
            String userIdAndUserType = TokenVerification.getIdAndSubject(token);
            String userId = userIdAndUserType.split(",", 2)[0];
            LockManager.getInstance().releaseAll(userId);

            if(!token.isEmpty()){
                TokenVerification.removeToken(token);
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
                out.print(jsonObject);
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

        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
        }

    }

}
