package controllers;

import domain.Exam;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exam-controller")
public class examController extends HttpServlet {
    private static final long serialVersionUID = 2L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public examController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examID = request.getParameter("exam_id");

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectId = request.getParameter("subject_id");
        String examName = request.getParameter("examName");

        Exam exam = new Exam();
        exam.setSubjectID(subjectId);
        exam.setExamName(examName);
        exam.setId(KeyGenerator.getSingletonInstance().getKey(exam));

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerNew(exam);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
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
        String examID =request.getParameter("exam_id");

        Exam exam = new Exam();
        exam.setId(examID);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(exam);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();

    }

}
