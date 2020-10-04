package controllers;

import domain.Exam;
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

@WebServlet("/api/exam-controller")
public class examController extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private enum EXAMSTATUS {PUBLISH, CLOSE, UPDATE};
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

        if(status.equals(EXAMSTATUS.PUBLISH.toString())){
            // change the exam status from publish to opening
        } else if (status.equals(EXAMSTATUS.CLOSE.toString())){
            // change the exam status from opening to closed
            // find all students who're taking this exam and not submitted yet
            // change their submissions to empty submission.
        } else if (status.equals(EXAMSTATUS.UPDATE.toString())){
            // read json req, update corresponding field to db
        } else {
            // wrong status
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            out.print(jsonObject);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.flush();
        }

//        Exam exam = new Exam();
//        exam.setSubjectID(subjectId);
//        exam.setExamName(examName);
//        exam.setId(KeyGenerator.getSingletonInstance().getKey(exam));
//
//        UnitOfWork.newCurrent();
//        UnitOfWork.getCurrent().registerNew(exam);
//        UnitOfWork.getCurrent().commit();


        out.print("Success");
        out.flush();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examID =request.getParameter("exam_id");
        String subjectId = request.getParameter("subject_id");
        String examName = request.getParameter("examName");

        Exam exam = new Exam();
        exam.setSubjectID(subjectId);
        exam.setExamName(examName);
        exam.setId(examID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(exam);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
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
