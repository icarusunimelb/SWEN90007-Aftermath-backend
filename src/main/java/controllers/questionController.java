package controllers;

import com.google.gson.Gson;
import domain.Exam;
import domain.Question;
import org.json.JSONObject;
import utils.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/question-controller")
public class questionController extends HttpServlet {
    private static final long serialVersionUID = 5L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public questionController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // /api/question-controller?dataId=examId
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if(TokenVerification.validLecturer(request, response) == TokenVerification.ERRORFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }


        String examId = request.getParameter("dataId");
        Exam exam = new Exam();
        exam.setId(examId);
        exam.load();


        List<Question> questions = exam.getQuestions();
        if(questions.size() == 0){
            JSONObject jsonObject = new JSONObject(
                    "{[]}");
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } else {

            String json = new Gson().toJson(questions);
            String newJson = json.replace("id", "dataId");
            out.print(newJson);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        }


    }

    /**
     * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String questionBody = request.getParameter("questionBody");
//        String examID = request.getParameter("exam_id");
//
//        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
//        multipleChoiceQuestion.setQuestionBody(questionBody);
//        multipleChoiceQuestion.setExamID(examID);
//        multipleChoiceQuestion.setId(KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestion));
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerNew(multipleChoiceQuestion);
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String id = request.getParameter("mcq_id");
//        String questionBody = request.getParameter("questionBody");
//        String examID = request.getParameter("exam_id");
//
//        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
//        multipleChoiceQuestion.setQuestionBody(questionBody);
//        multipleChoiceQuestion.setExamID(examID);
//        multipleChoiceQuestion.setId(id);
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestion);
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();
    }


    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String id = request.getParameter("mcq_id");
//
//        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion() ;
//        multipleChoiceQuestion.setId(id);
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerDeleted(multipleChoiceQuestion);
//        UnitOfWork.getCurrent().commit();
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        out.print("Success");
//        out.flush();
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "authorization");
    }
}