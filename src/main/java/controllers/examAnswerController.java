package controllers;

import datamapper.*;
import domain.*;
import exceptions.RecordNotExistException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import utils.KeyGenerator;
import utils.LockManager;
import security.TokenVerification;
import utils.USERTYPE;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@WebServlet("/api/examAnswer-controller")
public class examAnswerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private enum EXAMSTATUS {publish, close, update};
    /**
     * @see HttpServlet#HttpServlet()
     */
    public examAnswerController() {
        super();
    }

     // update exam answer marks
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            String requestData = request.getReader().lines().collect(Collectors.joining());

            JSONObject jsonObject1 = new JSONObject(requestData);
            String examId = jsonObject1.getString("examId");
            JSONArray examAnswerArray = jsonObject1.getJSONArray("markings");

            LockManager.getInstance().acquireLock(examId, Thread.currentThread().getName());
            UnitOfWork.newCurrent();

            for ( int i = 0; i < examAnswerArray.length(); i ++){
                JSONObject examAnswerJson = (JSONObject) examAnswerArray.get(i);

                String examAnswerId = examAnswerJson.getString("dataId");
                int marks = examAnswerJson.getInt("marks");

                Exam exam = ExamMapper.getSingletonInstance().findWithID(examId);
                exam.setStatus("MARKED");
                UnitOfWork.getCurrent().registerDirty(exam);

                ExamAnswer examAnswer = ExamAnswerMapper.getSingletonInstance().findWithID(examAnswerId);
                examAnswer.setFinalMark(marks);
                UnitOfWork.getCurrent().registerDirty(examAnswer);

                JSONObject answersJsonObject = examAnswerJson.getJSONObject("answers");
                Iterator keys = answersJsonObject.keys();
                while(keys.hasNext()){
                    String questionId = (String) keys.next();
                    if(answersJsonObject.get(questionId) instanceof JSONObject){
                        JSONObject answerJson = (JSONObject) answersJsonObject.get(questionId);
                        // String answer = answerJson.getString("answer");
                        int mark = answerJson.getInt("mark");

                        if(questionId.contains("Multiple")){
                            MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswer =
                                    MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithTwoID(examAnswerId, questionId);
                            multipleChoiceQuestionAnswer.setMark(mark);
                            UnitOfWork.getCurrent().registerDirty(multipleChoiceQuestionAnswer);
                        } else {
                            ShortAnswerQuestionAnswer shortAnswerQuestionAnswer =
                                    ShortAnswerQuestionAnswerMapper.getSingletonInstance().findWithTwoID(examAnswerId, questionId);
                            shortAnswerQuestionAnswer.setMark(mark);
                            UnitOfWork.getCurrent().registerDirty(shortAnswerQuestionAnswer);
                        }
                    }
                }

            }

            try {
                UnitOfWork.getCurrent().commit();
            } catch (RecordNotExistException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            LockManager.getInstance().releaseLock(examId, Thread.currentThread().getName());

            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */ // student submits an exam answer
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            UnitOfWork.newCurrent();

            String token = TokenVerification.getTokenFromHeader(request);
            String userIdAndUserType = TokenVerification.getIdAndSubject(token);

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
            //System.out.println("Status1: "+exam.getStatus());
            if (exam.getStatus().equals("PUBLISHED") || exam.getStatus().equals("ONGOING")) {
                exam.setStatus("ONGOING");
            }else{
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
                out.print(jsonObject);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.flush();
                return;
            }

            for(int i = 0; i< answers.length(); i++){
                JSONObject examAnswerJson = (JSONObject) answers.get(i);
                // System.out.println("this is your exam answer" + examAnswerJson.toString());
                String questionId = examAnswerJson.getString("questionId");
                String answer = "";
                int newAnswer = -1;
                try{
                    answer = examAnswerJson.getString("answer");
                } catch(JSONException e){
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
                System.out.println("students num: "+(submitted.size() + 1));
                exam.setStatus("CLOSED");
            }
            System.out.println("Status2: "+exam.getStatus());

            UnitOfWork.getCurrent().registerDirty(exam);


//         validate this examAnswer
            try {
                UnitOfWork.getCurrent().commit();
            } catch (RecordNotExistException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }


            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RecordNotExistException e) {
            e.printStackTrace();
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
