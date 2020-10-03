package controllers;

import domain.ShortAnswerQuestion;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/short-answer-question-controller")
public class sQuestionController extends HttpServlet {
    private static final long serialVersionUID = 7L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public sQuestionController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // /choice-controller?subject_code=1&exam_id=2&question_id=3
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionBody = request.getParameter("questionBody");
        String id = request.getParameter("saq_id");
        String examID = request.getParameter("exam_id");

        ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
        shortAnswerQuestion.setQuestionBody(questionBody);
        shortAnswerQuestion.setExamID(examID);
        shortAnswerQuestion.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(shortAnswerQuestion);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionBody = request.getParameter("questionBody");
        String examID = request.getParameter("exam_id");

        ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
        shortAnswerQuestion.setQuestionBody(questionBody);
        shortAnswerQuestion.setExamID(examID);
        shortAnswerQuestion.setId(KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestion));

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(shortAnswerQuestion);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("saq_id");


        ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
        shortAnswerQuestion.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(shortAnswerQuestion);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

}