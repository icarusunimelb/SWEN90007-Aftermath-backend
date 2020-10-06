package controllers;

import domain.MultipleChoiceQuestionAnswer;
import domain.ShortAnswerQuestionAnswer;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/short-answer-question-answer-controller")
public class sQAnswerController extends HttpServlet {
    private static final long serialVersionUID = 6L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public sQAnswerController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswer = new ShortAnswerQuestionAnswer();
        String id = request.getParameter("saqa_id");;
        String answer = request.getParameter("answer");
        String questionID = request.getParameter("question_id");
        String examAnswerID = request.getParameter("exam_answer_id");
        int mark = Integer.parseInt(request.getParameter("mark"));
        shortAnswerQuestionAnswer.setId(id);
        shortAnswerQuestionAnswer.setAnswer(answer);
        shortAnswerQuestionAnswer.setMark(mark);
        shortAnswerQuestionAnswer.setQuestionID(questionID);
        shortAnswerQuestionAnswer.setExamAnswerID(examAnswerID);
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(shortAnswerQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswer = new ShortAnswerQuestionAnswer();
        String id = KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestionAnswer);
        String answer = request.getParameter("answer");
        String questionID = request.getParameter("question_id");
        String examAnswerID = request.getParameter("exam_answer_id");
        int mark = Integer.parseInt(request.getParameter("mark"));
        shortAnswerQuestionAnswer.setId(id);
        shortAnswerQuestionAnswer.setAnswer(answer);
        shortAnswerQuestionAnswer.setMark(mark);
        shortAnswerQuestionAnswer.setQuestionID(questionID);
        shortAnswerQuestionAnswer.setExamAnswerID(examAnswerID);
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(shortAnswerQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswer = new ShortAnswerQuestionAnswer();
        String id = request.getParameter("saqa_id");;

        shortAnswerQuestionAnswer.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(shortAnswerQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization");
    }

}
