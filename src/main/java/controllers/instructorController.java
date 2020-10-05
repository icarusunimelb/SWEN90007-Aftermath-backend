package controllers;

import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.Instructor;
import domain.Name;
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

@WebServlet("/instructor-controller")
public class instructorController extends HttpServlet {
    private static final long serialVersionUID = 3L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public instructorController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (!InstructorMapper.getSingletonInstance().authenticate(email, password).isEmpty()) {
            out.print("Success");
        }else {
            out.print("Wrong email or password");
        }

        out.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (!InstructorMapper.getSingletonInstance().registerOrNot(email)) {
            Instructor instructor = new Instructor();
            instructor.setEmail(email);
            instructor.setPassword(password);
            instructor.setName(new Name(firstName, lastName));
            instructor.setId(KeyGenerator.getSingletonInstance().getKey(instructor));
            UnitOfWork.newCurrent();
            UnitOfWork.getCurrent().registerNew(instructor);
            UnitOfWork.getCurrent().commit();
            out.print("Success");
        }else {
            out.print("Already registered");
        }
        out.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("instructor_id");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");

        Instructor instructor = new Instructor();
        instructor.setEmail(email);
        instructor.setPassword(password);
        instructor.setName(new Name(firstName, lastName));
        instructor.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(instructor);
        UnitOfWork.getCurrent().commit();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        out.print("Success");
        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("instructor_id");

        Instructor instructor = new Instructor();
        instructor.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(instructor);
        UnitOfWork.getCurrent().commit();

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
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

}
