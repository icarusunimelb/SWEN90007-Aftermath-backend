package controllers;

import domain.Subject;
import org.json.JSONObject;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class addSubjectController
 */
@WebServlet("/subject-controller")
public class subjectController extends HttpServlet {
	private static final long serialVersionUID = 8L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public subjectController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("subject_id");
		String subjectCode = request.getParameter("subject_code");
		String subjectName = request.getParameter("subject_name");

		Subject subject = new Subject();
		subject.setSubjectCode(subjectCode);
		subject.setSubjectName(subjectName);
		subject.setId(id);

		UnitOfWork.newCurrent();
		UnitOfWork.getCurrent().registerDirty(subject);

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		out.print("Success");
		out.flush();
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String subjectCode = request.getParameter("subject_code");
		String subjectName = request.getParameter("subject_name");

		Subject subject = new Subject();
		subject.setSubjectCode(subjectCode);
		subject.setSubjectName(subjectName);
		subject.setId(KeyGenerator.getSingletonInstance().getKey(subject));

		UnitOfWork.newCurrent();
		UnitOfWork.getCurrent().registerNew(subject);

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		out.print("Success");
		out.flush();
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("subject_id");

		Subject subject = new Subject();
		subject.setId(id);

		UnitOfWork.newCurrent();
		UnitOfWork.getCurrent().registerDeleted(subject);

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		JSONObject jsonObject = new JSONObject(String.format(
				"{\"code\":\"%s\"}",HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		out.print(jsonObject);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		out.flush();
	}

}
