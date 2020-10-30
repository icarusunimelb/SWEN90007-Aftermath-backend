package controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import security.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import  org.json.*;

@CrossOrigin(origins = "*", maxAge = 3600)
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try{
            String token = TokenVerification.getTokenFromHeader(request);

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

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization");
    }
}
