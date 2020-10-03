package controllers;


import com.google.gson.Gson;
import datamapper.ChoiceMapper;
import domain.Choice;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.*;

@WebServlet("/choice-controller")
public class choiceController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public choiceController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionID = request.getParameter("question_id");
        String choiceString = request.getParameter("choice");
        Choice choice = new Choice();
        choice.setChoice(choiceString);
        choice.setQuestionID(questionID);
        choice.setId(KeyGenerator.getSingletonInstance().getKey(choice));

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(choice);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject reply = new JSONObject();
        reply.append("Status", "Success");
        Gson gson = new Gson();
        out.print(gson.toJson(reply));
        out.flush();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choiceID = request.getParameter("choice_id");
        String questionID = request.getParameter("question_id");
        String choiceString = request.getParameter("choice");
        Choice choice = new Choice();
        choice.setChoice(choiceString);
        choice.setQuestionID(questionID);
        choice.setId(choiceID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(choice);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject reply = new JSONObject();
        reply.append("Status", "Success");
        Gson gson = new Gson();
        out.print(gson.toJson(reply));
        out.flush();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choiceID = request.getParameter("choice_id");
        Choice choice = new Choice();
        choice.setId(choiceID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(choice);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject reply = new JSONObject();
        reply.append("Status", "Success");
        Gson gson = new Gson();
        out.print(gson.toJson(reply));
        out.flush();

    }

}