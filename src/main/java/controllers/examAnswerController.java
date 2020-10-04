package controllers;

import com.google.gson.Gson;
import datamapper.ExamAnswerMapper;
import datamapper.MultipleChoiceQuestionMapper;
import datamapper.ShortAnswerQuestionMapper;
import domain.*;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.KeyGenerator;
import utils.TokenVerification;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;


@WebServlet("/api/examAnswer-controller")
public class examAnswerController extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private enum EXAMSTATUS {publish, close, update};
    /**
     * @see HttpServlet#HttpServlet()
     */
    public examAnswerController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */ // /api/exam-controller?dataId=123456
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String examId = request.getParameter("dataId");
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//
//        String token = TokenVerification.getTokenFromHeader(request);
//        String userIdAndUserType = TokenVerification.verifyToken(token);
//
//        String userId = userIdAndUserType.split(",", 2)[0];
//        String userType = userIdAndUserType.split(",", 2)[1];
//
//        Exam exam = new Exam();
//
//
//        JSONObject jsonObject = new JSONObject(String.format(
//                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
//        out.print(jsonObject);
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        out.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        String token = TokenVerification.getTokenFromHeader(request);
        String userIdAndUserType = TokenVerification.verifyToken(token);

        String userId = userIdAndUserType.split(",", 2)[0];

        String requestData = request.getReader().lines().collect(Collectors.joining());
        System.out.println("this is ur exam id: "+ requestData);
        JSONObject examJson = new JSONObject(requestData);
        String examId = examJson.getString("examId");
        JSONArray answers = examJson.getJSONArray("answers");

        ExamAnswer examAnswer = new ExamAnswer();
        examAnswer.setStudentID(userId);
        examAnswer.setExamID(examId);
        examAnswer.setId(KeyGenerator.getSingletonInstance().getKey(examAnswer));

        if(ExamAnswerMapper.getSingletonInstance().checkIfStudentAnswer(examAnswer.getExamID(), userId)){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
        }

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(examAnswer);
        UnitOfWork.getCurrent().commit();

        for(int i = 0; i< answers.length(); i++){
            JSONObject examAnswerJson = (JSONObject) answers.get(i);
            System.out.println("this is your exam answer" + examAnswerJson.toString());
            String questionId = examAnswerJson.getString("questionId");
            String answer = examAnswerJson.getString("answer");

            // todo find the question
            MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestionMapper.getSingletonInstance().findWithID(questionId);
            if(multipleChoiceQuestion.getId() == null){
                ShortAnswerQuestion shortAnswerQuestion = ShortAnswerQuestionMapper.getSingletonInstance().findWithID(questionId);
                ShortAnswerQuestionAnswer shortAnswerQuestionAnswer= new ShortAnswerQuestionAnswer();
                shortAnswerQuestionAnswer.setAnswer(answer);
                shortAnswerQuestionAnswer.setQuestionID(questionId);
                shortAnswerQuestionAnswer.setExamAnswerID(examAnswer.getId());
                shortAnswerQuestionAnswer.setShortAnswerQuestion(shortAnswerQuestion);
                shortAnswerQuestionAnswer.setId(KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestionAnswer));
                UnitOfWork.getCurrent().registerNew(shortAnswerQuestionAnswer);
            } else{
                MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer= new MultipleChoiceQuestionAnswer();

                Choice choice = new Choice();
                choice.setChoice(answer);
                choice.setQuestionID(multipleChoiceQuestionAnswer.getId());
                choice.setId(KeyGenerator.getSingletonInstance().getKey(choice));
                UnitOfWork.getCurrent().commit();

                multipleChoiceQuestionAnswer.setChosenAnswer(choice);
                multipleChoiceQuestionAnswer.setExamAnswerID(examAnswer.getId());
                multipleChoiceQuestionAnswer.setQuestionID(questionId);
                multipleChoiceQuestionAnswer.setMultipleChoiceQuestion(multipleChoiceQuestion);
                multipleChoiceQuestionAnswer.setId(KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestionAnswer));
                UnitOfWork.getCurrent().registerNew(multipleChoiceQuestionAnswer);
                UnitOfWork.getCurrent().commit();
            }
        }



//         validate this examAnswer

        String key = KeyGenerator.getSingletonInstance().getKey(examAnswer);
        examAnswer.setId(key);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(examAnswer);
        UnitOfWork.getCurrent().commit();



        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String examID =request.getParameter("dataId");
//
//        Exam exam = new Exam();
//        exam.setId(examID);
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerDeleted(exam);
//        UnitOfWork.getCurrent().commit();
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        JSONObject jsonObject = new JSONObject(String.format(
//                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
//        out.print(jsonObject);
//        response.setStatus(HttpServletResponse.SC_OK);
//        out.flush();

    }


}
