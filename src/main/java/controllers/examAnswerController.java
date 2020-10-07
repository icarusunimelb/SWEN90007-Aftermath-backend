package controllers;

import datamapper.*;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

     // update exam answer marks
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        if(TokenVerification.validLecturer(request, response) == TokenVerification.ERRORFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }
        UnitOfWork.newCurrent();

        String requestData = request.getReader().lines().collect(Collectors.joining());
        JSONArray examAnswerArray = new JSONArray(requestData);
//
        for ( int i = 0; i < examAnswerArray.length(); i ++){
            JSONObject examAnswerJson = (JSONObject) examAnswerArray.get(i);

            String examAnswerId = examAnswerJson.getString("dataId");
            int marks = examAnswerJson.getInt("marks");
            String examId = examAnswerJson.getString("examId");

            Exam exam = ExamMapper.getSingletonInstance().findWithID(examId);
            exam.setStatus("MARKED");
            UnitOfWork.getCurrent().registerDirty(exam);

            ExamAnswer examAnswer = ExamAnswerMapper.getSingletonInstance().findWithID(examAnswerId);
            examAnswer.load();
            examAnswer.setFinalMark(marks);
            UnitOfWork.getCurrent().registerDirty(examAnswer);

            JSONObject answersJsonObject = examAnswerJson.getJSONObject("answers");
            Iterator keys = answersJsonObject.keys();
            while(keys.hasNext()){
                String questionAnswerId = (String) keys.next();
                if(answersJsonObject.get(questionAnswerId) instanceof JSONObject){
                    JSONObject answerJson = (JSONObject) answersJsonObject.get(questionAnswerId);
                    String answer = answerJson.getString("answer");
                    int mark = answerJson.getInt("mark");

                    if(questionAnswerId.contains("Multiple")){
                        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer = MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithID(questionAnswerId);
                        multipleChoiceQuestionAnswer.setMark(mark);
                        UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestionAnswer);
                    } else {
                        ShortAnswerQuestionAnswer shortAnswerQuestionAnswer = ShortAnswerQuestionAnswerMapper.getSingletonInstance().findWithID(questionAnswerId);
                        shortAnswerQuestionAnswer.setMark(mark);
                        UnitOfWork.getCurrent().registerDirty(shortAnswerQuestionAnswer);
                    }
                }
            }

        }
        UnitOfWork.getCurrent().commit();

        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();
        return;




//        UnitOfWork.newCurrent();
//
//        String requestData = request.getReader().lines().collect(Collectors.joining());
//        JSONArray examAnswerArray = new JSONArray(requestData);
////
//        for ( int i = 0; i < examAnswerArray.length(); i ++){
//            JSONObject examAnswerJson = (JSONObject) examAnswerArray.get(i);
//
//            String examAnswerId = examAnswerJson.getString("dataId");
//            String marks = examAnswerJson.getString("marks");
//
//            ExamAnswer examAnswer = ExamAnswerMapper.getSingletonInstance().findWithID(examAnswerId);
//            examAnswer.load();
//            examAnswer.setFinalMark(Double.parseDouble(marks));
//            UnitOfWork.getCurrent().registerDirty(examAnswer);
//            JSONArray answersJsonArray = examAnswerJson.getJSONArray("answers");
//            for ( int j = 0; j < answersJsonArray.length(); j ++){
//                JSONObject answerJson = (JSONObject) examAnswerArray.get(i);
//                String answerId = answerJson.getString("dataId");
//                String mark = answerJson.getString("mark");
//                ShortAnswerQuestionAnswer shortAnswerQuestionAnswer = ShortAnswerQuestionAnswerMapper.getSingletonInstance().findWithID(answerId);
//                if(shortAnswerQuestionAnswer.getId() == null || shortAnswerQuestionAnswer.getId().equals("")){
//                    MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer = MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithID(examAnswerId);
//                    multipleChoiceQuestionAnswer.setMark(Double.parseDouble(mark));
//                    UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestionAnswer);
//                } else {
//                    shortAnswerQuestionAnswer.setMark(Double.parseDouble(mark));
//                    UnitOfWork.getCurrent().registerDirty(shortAnswerQuestionAnswer);
//                }
//            }
//
//            UnitOfWork.getCurrent().commit();
//        }
//
//        JSONObject jsonObject = new JSONObject(String.format(
//                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
//        out.print(jsonObject);
//        response.setStatus(HttpServletResponse.SC_OK);
//        out.flush();
//        return;

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */ // student submits an exam answer
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        if(TokenVerification.validLecturer(request, response) != TokenVerification.STUDENTFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }

        UnitOfWork.newCurrent();

        String token = TokenVerification.getTokenFromHeader(request);
        String userIdAndUserType = TokenVerification.verifyToken(token);

        String userId = userIdAndUserType.split(",", 2)[0];

        String requestData = request.getReader().lines().collect(Collectors.joining());
        JSONObject examJson = new JSONObject(requestData);
        String examId = examJson.getString("examId");
        JSONArray answers = examJson.getJSONArray("answers");

        ExamAnswer examAnswer = new ExamAnswer();
        examAnswer.setStudentID(userId);
        examAnswer.setExamID(examId);
        examAnswer.setFinalMark(-1);
        examAnswer.setId(KeyGenerator.getSingletonInstance().getKey(examAnswer));

        UnitOfWork.getCurrent().registerNew(examAnswer);


        Exam exam = ExamMapper.getSingletonInstance().findWithID(examId);
        System.out.println("Status1: "+exam.getStatus());
        if (exam.getStatus() == "PUBLISHED" || exam.getStatus() == "ONGOING") {
            exam.setStatus("ONGOING");
        }else{
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
            return;
        }

//        if(ExamAnswerMapper.getSingletonInstance().checkIfStudentAnswer(examAnswer.getExamID(), userId)){
//            JSONObject jsonObject = new JSONObject(String.format(
//                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
//            out.print(jsonObject);
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            out.flush();
//            return;
//        }



        for(int i = 0; i< answers.length(); i++){
            JSONObject examAnswerJson = (JSONObject) answers.get(i);
            // System.out.println("this is your exam answer" + examAnswerJson.toString());
            String questionId = examAnswerJson.getString("questionId");
            String answer = "";
            int newAnswer = -1;
            try{
                answer = examAnswerJson.getString("answer");
            } catch(org.json.JSONException e){
                System.out.println("JSON EXCEPTION ERROR: "+ e.toString());
                newAnswer = examAnswerJson.getInt("answer");
            }
            int mark = -1;

            // todo find the question

            MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestionMapper.getSingletonInstance().findWithID(questionId);
            ShortAnswerQuestion shortAnswerQuestion = ShortAnswerQuestionMapper.getSingletonInstance().findWithID(questionId);

            if(multipleChoiceQuestion.getId() == null){
                // it's a short answer question
                ShortAnswerQuestionAnswer shortAnswerQuestionAnswer= new ShortAnswerQuestionAnswer();
                shortAnswerQuestionAnswer.setAnswer(answer);
                shortAnswerQuestionAnswer.setQuestionID(questionId);
                shortAnswerQuestionAnswer.setMark(mark);
                shortAnswerQuestionAnswer.setExamAnswerID(examAnswer.getId());
                shortAnswerQuestionAnswer.setShortAnswerQuestion(shortAnswerQuestion);
                shortAnswerQuestionAnswer.setId(KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestionAnswer));
                UnitOfWork.getCurrent().registerNew(shortAnswerQuestionAnswer);
            } else{
                MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer= new MultipleChoiceQuestionAnswer();

                multipleChoiceQuestionAnswer.setAnswerIndex(newAnswer);
                multipleChoiceQuestionAnswer.setExamAnswerID(examAnswer.getId());
                multipleChoiceQuestionAnswer.setQuestionID(questionId);
                multipleChoiceQuestionAnswer.setMark(mark);
                multipleChoiceQuestionAnswer.setMultipleChoiceQuestion(multipleChoiceQuestion);
                multipleChoiceQuestionAnswer.setId(KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestionAnswer));
                UnitOfWork.getCurrent().registerNew(multipleChoiceQuestionAnswer);
            }

//            UnitOfWork.getCurrent().commit();
        }

        System.out.println("Status3: "+exam.getStatus());
        List<ExamAnswer> submitted = exam.getExamAnswers();
        if (submitted.size() + 1 == StudentMapper.getSingletonInstance().findWithSubjectID(exam.getSubjectID()).size()) {
            exam.setStatus("CLOSED");
        }
        System.out.println("Status2: "+exam.getStatus());

        UnitOfWork.getCurrent().registerDirty(exam);


//         validate this examAnswer

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

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization");
    }

}
