package controllers;

import domain.MultipleChoiceQuestionAnswer;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/multiple-choice-question-answer-controller")
public class mQAnswerController extends HttpServlet {
    private static final long serialVersionUID = 4L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public mQAnswerController() {
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
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer = new MultipleChoiceQuestionAnswer();
        String id = request.getParameter("mcqa_id");
        String chosenAnswerID = request.getParameter("chosen_answer_id");
        String questionID = request.getParameter("question_id");
        String examAnswerID = request.getParameter("exam_answer_id");
        double mark = Double.parseDouble(request.getParameter("mark"));
        multipleChoiceQuestionAnswer.setId(id);
        multipleChoiceQuestionAnswer.setMark(mark);
        multipleChoiceQuestionAnswer.setQuestionID(questionID);
        multipleChoiceQuestionAnswer.setExamAnswerID(examAnswerID);
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    /**
     * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer = new MultipleChoiceQuestionAnswer();
        String id = KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestionAnswer);
        String chosenAnswerID = request.getParameter("chosen_answer_id");
        String questionID = request.getParameter("question_id");
        String examAnswerID = request.getParameter("exam_answer_id");
        double mark = Double.parseDouble(request.getParameter("mark"));
        multipleChoiceQuestionAnswer.setId(id);

        multipleChoiceQuestionAnswer.setMark(mark);
        multipleChoiceQuestionAnswer.setQuestionID(questionID);
        multipleChoiceQuestionAnswer.setExamAnswerID(examAnswerID);
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(multipleChoiceQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer = new MultipleChoiceQuestionAnswer();
        String id = request.getParameter("mcqa_id");

        multipleChoiceQuestionAnswer.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(multipleChoiceQuestionAnswer);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "authorization");
    }

}

