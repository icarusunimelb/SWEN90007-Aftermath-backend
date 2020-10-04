package controllers;

import com.google.gson.Gson;
import datamapper.StudentMapper;
import domain.Exam;
import domain.ExamAnswer;
import domain.Student;
import domain.User;
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
import java.security.Key;
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


        BufferedReader reader = request.getReader();
        Exam exam = new Exam();
        String key = KeyGenerator.getSingletonInstance().getKey(exam);
        exam.setId(key);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(exam);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examID =request.getParameter("dataId");

        Exam exam = new Exam();
        exam.setId(examID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(exam);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject jsonObject = new JSONObject(String.format(
                "{\"code\":\"%s\"}",HttpServletResponse.SC_OK));
        out.print(jsonObject);
        response.setStatus(HttpServletResponse.SC_OK);
        out.flush();

    }


}
