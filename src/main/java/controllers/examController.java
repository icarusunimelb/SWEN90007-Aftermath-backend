package controllers;

import datamapper.*;
import domain.*;
import exceptions.RecordNotExistException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.KeyGenerator;
import utils.LockManager;
import security.TokenVerification;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            String examId = request.getParameter("dataId");

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            // get the lock of current exam
            LockManager.getInstance().acquireLock(examId, Thread.currentThread().getName());

            Exam exam = new Exam();
            exam.setId(examId);

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
                    examAnswer.setFinalMark(-1);

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

                // delete exam questions, choices

                List<Question> currentQuestions = new ArrayList<>();
                currentQuestions.addAll(MultipleChoiceQuestionMapper.getSingletonInstance().findWithExamID(examId));

                for (int i = 0; i < currentQuestions.size(); i++){
                    List<Choice> currentChoices = ChoiceMapper.getSingletonInstance().findWithQuestionID(currentQuestions.get(i).getId());
                    for (int j = 0; j < currentChoices.size(); j++){
                        UnitOfWork.getCurrent().registerDeleted(currentChoices.get(j));
                    }

                }

                currentQuestions.addAll(ShortAnswerQuestionMapper.getSingletonInstance().findWithExamID(examId));
                for (int i = 0; i < currentQuestions.size(); i++){
                    UnitOfWork.getCurrent().registerDeleted(currentQuestions.get(i));
                }


                UnitOfWork.getCurrent().registerDirty(exam);

                JSONArray questionsArray = examJson.getJSONArray("questions");

                for(int i = 0; i< questionsArray.length(); i++){
                    JSONObject questionJson = (JSONObject) questionsArray.get(i);
                    String questionType = questionJson.getString("type");
                    String questionDescription = questionJson.getString("description");
                    int questionMarks = questionJson.getInt("marks");
                    String questionTitle = questionJson.getString("title");
                    String questionId;

                    if(questionType.equals("QUESTION_MULTIPLE_CHOICE")){


                        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
                        questionId = KeyGenerator.getSingletonInstance().getKey(multipleChoiceQuestion);


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
                            choice.setIndex(j+1);
                            choice.setId(KeyGenerator.getSingletonInstance().getKey(choice));
                            UnitOfWork.getCurrent().registerNew(choice);

                            //System.out.println("this is your first choice "+ choiceJson);
                        }
                    } else {
                        ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
                        questionId = KeyGenerator.getSingletonInstance().getKey(shortAnswerQuestion);
                        shortAnswerQuestion.setExamID(examId);
                        shortAnswerQuestion.setQuestionBody(questionDescription);
                        shortAnswerQuestion.setId(questionId);
                        shortAnswerQuestion.setTitle(questionTitle);
                        shortAnswerQuestion.setTotalMark(questionMarks);

                        UnitOfWork.getCurrent().registerNew(shortAnswerQuestion);
                    }

                    //System.out.println("this is your exam answer" + questionJson.toString());
                }

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
            try {
                UnitOfWork.getCurrent().commit();
            } catch (RecordNotExistException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            // release the lock of current exam
            LockManager.getInstance().releaseLock(examId, Thread.currentThread().getName());

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

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            String requestData = request.getReader().lines().collect(Collectors.joining());
            JSONObject examJson = new JSONObject(requestData);
            String subjectId = examJson.getString("subjectId");
            String examName = "new-exam";
            try{
                examName = examJson.getString("examName");
            }catch (JSONException e){
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
            try {
                UnitOfWork.getCurrent().commit();
            } catch (RecordNotExistException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }


            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\",\"examId\":\"%s\"}",HttpServletResponse.SC_OK, exam.getId()));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            String examID =request.getParameter("dataId");

            // get the lock for the exam
            LockManager.getInstance().acquireLock(examID, Thread.currentThread().getName());

            Exam exam = new Exam();
            exam.setId(examID);

            UnitOfWork.newCurrent();

            // delete questions
            for (Question question : exam.getQuestions()) {
                if (question instanceof MultipleChoiceQuestion) {
                    // delete choices
                    for (Choice multipleChoice : ((MultipleChoiceQuestion) question).getMultipleChoices()) {
                        UnitOfWork.getCurrent().registerDeleted(multipleChoice);
                    }
                }
                UnitOfWork.getCurrent().registerDeleted(question);
            }

            UnitOfWork.getCurrent().registerDeleted(exam);
            try {
                UnitOfWork.getCurrent().commit();
            } catch (RecordNotExistException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            // release the lock for the exam
            LockManager.getInstance().releaseLock(examID, Thread.currentThread().getName());


            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_OK);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
