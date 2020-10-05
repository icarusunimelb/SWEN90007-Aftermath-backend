package controllers;

import domain.MultipleChoiceQuestion;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/multiple-choice-question-controller")
public class mQuestionController extends HttpServlet {
    private static final long serialVersionUID = 5L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public mQuestionController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // /choice-controller?subject_code=1&exam_id=2&question_id=3
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionBody = request.getParameter("questionBody");
        String examID = request.getParameter("exam_id");

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
        multipleChoiceQuestion.setQuestionBody(questionBody);
        multipleChoiceQuestion.setExamID(examID);
        multipleChoiceQuestion.setId(KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestion));

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(multipleChoiceQuestion);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("mcq_id");
        String questionBody = request.getParameter("questionBody");
        String examID = request.getParameter("exam_id");

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
        multipleChoiceQuestion.setQuestionBody(questionBody);
        multipleChoiceQuestion.setExamID(examID);
        multipleChoiceQuestion.setId(id);
        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestion);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }


    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("mcq_id");

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
        multipleChoiceQuestion.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(multipleChoiceQuestion);
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