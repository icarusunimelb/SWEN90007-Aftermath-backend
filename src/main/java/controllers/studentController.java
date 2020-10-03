package controllers;

import datamapper.StudentMapper;
import domain.Name;
import domain.Student;
import org.json.JSONObject;
import utils.KeyGenerator;
import utils.UnitOfWork;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/student-controller")
public class studentController extends HttpServlet {
    private static final long serialVersionUID = 7L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public studentController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        JSONObject object = new JSONObject(data);
        String email = object.getString("username");
        String password = object.getString("password");*/
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(email);
        System.out.println(password);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StudentMapper.getSingletonInstance().authenticate(email, password)) {
            out.print("Success");
        }else {
            out.print("Wrong email or password");
        }
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");


        if (!StudentMapper.getSingletonInstance().registerOrNot(email)) {
            Student student = new Student();
            student.setId(KeyGenerator.getSingletonInstance().getKey(student));
            student.setEmail(email);
            student.setName(new Name(firstName, lastName));
            student.setPassword(password);

            UnitOfWork.newCurrent();
            UnitOfWork.getCurrent().registerNew(student);
            UnitOfWork.getCurrent().commit();

            out.print("Success");
        }else {
            out.print("Already registered");
        }

        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("student_id");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        Student student = new Student();
        student.setId(id);
        student.setEmail(email);
        student.setName(new Name(firstName, lastName));
        student.setPassword(password);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDirty(student);
        UnitOfWork.getCurrent().commit();

        out.print("Success");

        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("student_id");


        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        Student student = new Student();
        student.setId(id);

        UnitOfWork.newCurrent();
        UnitOfWork.getCurrent().registerDeleted(student);
        UnitOfWork.getCurrent().commit();

        out.print("Success");

        out.flush();
    }

}

