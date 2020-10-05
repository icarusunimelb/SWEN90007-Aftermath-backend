package controllers;

import datamapper.ExamMapper;
import datamapper.StudentMapper;
import domain.*;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.KeyGenerator;
import utils.tokenVerification;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/exam-controller")
public class examController extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private enum EXAMSTATUS {publish, close, update};
    /**
     * @see HttpServlet#HttpServlet()
     */
    public examController() {
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
     */ ///api/exam-controller?status=publish&dataId:123456
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String status = request.getParameter("status");
        String examId = request.getParameter("dataId");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if(tokenVerification.validLecturer(request, response) != tokenVerification.LECTURERFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }

        Exam exam = new Exam();
        exam.setId(examId);
        System.out.println("this is your data id" + examId);
        exam.load();

        UnitOfWork.newCurrent();

        if(status.equals(EXAMSTATUS.publish.toString())){
            // change the exam status from unpublished to published
            if(exam.getStatus().equals("UNPUBLISHED")){
                exam.setStatus("PUBLISHED");
            }

        } else if (status.equals(EXAMSTATUS.close.toString())){
            exam.setStatus("CLOSED");
            String subjectId = exam.getSubjectID();
            List<Student> allStudents = StudentMapper.getSingletonInstance().findWithSubjectID(subjectId);
            List<Student> notSubmittedStudents = StudentMapper.getSingletonInstance().getNotSubmittedStudents(allStudents, exam.getId());

            for (int i = 0; i < notSubmittedStudents.size(); i++){
                ExamAnswer examAnswer = new ExamAnswer();
                examAnswer.setExamID(exam.getId());
                examAnswer.setStudentID(notSubmittedStudents.get(i).getId());
                examAnswer.setId(KeyGenerator.getSingletonInstance().getKey(examAnswer));

                UnitOfWork.getCurrent().registerNew(examAnswer);

            }

            // change the exam status from opening to closed
            // find all students who're taking this exam and not submitted yet
            // change their submissions to empty submission.
        } else if (status.equals(EXAMSTATUS.update.toString())){
            String requestData = request.getReader().lines().collect(Collectors.joining());
            JSONObject examJson = new JSONObject(requestData);
            examId = examJson.getString("dataId");

            String examName = examJson.getString("examName");

            exam = ExamMapper.getSingletonInstance().findWithID(examId);
            exam.setExamName(examName);

            UnitOfWork.getCurrent().registerDirty(exam);

            JSONArray questionsArray = examJson.getJSONArray("questions");

            for(int i = 0; i< questionsArray.length(); i++){
                JSONObject questionJson = (JSONObject) questionsArray.get(i);
                String questionType = questionJson.getString("type");
                String questionDescription = questionJson.getString("description");
                Integer questionMarks = questionJson.getInt("marks");
                String questionTitle = questionJson.getString("title");
                String questionId = "";

                if(questionType.equals("QUESTION_MULTIPLE_CHOICE")){

                    try{
                        questionId = questionJson.getString("dataId");
                    } catch (org.json.JSONException e){
                        System.out.println("JSON Exception Error" + e.toString());
                    }
                    MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
                    if(questionId.isEmpty()){
                        questionId = KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestion);
                    }

                    multipleChoiceQuestion.setExamID(examId);
                    multipleChoiceQuestion.setTitle(questionTitle);
                    multipleChoiceQuestion.setTotalMark(questionMarks);
                    multipleChoiceQuestion.setQuestionBody(questionDescription);
                    multipleChoiceQuestion.setId(questionId);

                    UnitOfWork.getCurrent().registerNew(multipleChoiceQuestion);

                    JSONArray questionChoices = questionJson.getJSONArray("choices");

                    for(int j = 0; j< questionChoices.length(); j++){
                        String choiceJson = (String) questionChoices.get(j);
                        Choice choice = new Choice();
                        choice.setQuestionID(questionId);
                        choice.setChoice(choiceJson);
                        choice.setId(KeyGenerator.getSingletonInstance().getKey(choice));
                        UnitOfWork.getCurrent().registerNew(choice);

                        System.out.println("this is your first choice "+ choiceJson);
                    }
                } else {
                    try{
                        questionId = questionJson.getString("dataId");
                    } catch (org.json.JSONException e){
                        System.out.println("JSON Exception Error" + e.toString());
                    }
                    ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
                    if(questionId.isEmpty()){
                        questionId = KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestion);
                    }
                    shortAnswerQuestion.setExamID(examId);
                    shortAnswerQuestion.setQuestionBody(questionDescription);
                    shortAnswerQuestion.setId(questionId);
                    shortAnswerQuestion.setTitle(questionTitle);
                    shortAnswerQuestion.setTotalMark(questionMarks);

                    UnitOfWork.getCurrent().registerNew(shortAnswerQuestion);
                }

                System.out.println("this is your exam answer" + questionJson.toString());
            }
            UnitOfWork.getCurrent().commit();

            // todo confirm with front end about the format of request

            // read json req, update corresponding field to db
        } else {
            // wrong status
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
            return;
        }

        UnitOfWork.getCurrent().registerDirty(exam);
        UnitOfWork.getCurrent().commit();

        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

//        Exam exam = new Exam();
//        exam.setSubjectID(subjectId);
//        exam.setExamName(examName);
//        exam.setId(KeyGenerator.getSingletonInstance().getKey(exam));
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerNew(exam);
//        UnitOfWork.getCurrent().commit();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if(tokenVerification.validLecturer(request, response) == tokenVerification.ERRORFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }

        String requestData = request.getReader().lines().collect(Collectors.joining());
        JSONObject examJson = new JSONObject(requestData);
        String subjectId = examJson.getString("subjectId");
        String examName = "new-exam";
        try{
            examName = examJson.getString("examName");
        }catch (org.json.JSONException e){
            System.out.println("JSON Exception Error "+ e.toString());
        }

        Exam exam = new Exam();
        String key = KeyGenerator.getSingletonInstance().getKey(exam);
        exam.setId(key);
        exam.setSubjectID(subjectId);
        exam.setExamName(examName);
        exam.setStatus("UNPUBLISHED");

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(exam);
        UnitOfWork.getCurrent().commit();


        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\",\"examId\":\"%s\"}",HttpServletResponse.SC_OK, exam.getId()));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if(tokenVerification.validLecturer(request, response) == tokenVerification.ERRORFLAG){
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }

        String examID =request.getParameter("dataId");

        Exam exam = new Exam();
        exam.setId(examID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(exam);
        UnitOfWork.getCurrent().commit();


        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

}
