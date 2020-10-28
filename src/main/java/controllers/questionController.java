package controllers;

import DTO.DTOQuestion;
import com.google.gson.Gson;
import domain.Exam;
import domain.Question;
import org.json.JSONObject;
import security.TokenVerification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/question-controller")
public class questionController extends HttpServlet {
    private static final long serialVersionUID = 5L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public questionController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // /api/question-controller?dataId=examId
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");

            if (TokenVerification.validLecturer(request, response) == TokenVerification.ERRORFLAG) {
                JSONObject jsonObject = new JSONObject(String.format(
                        "{\"code\":\"%s\"}", HttpServletResponse.SC_UNAUTHORIZED));
                out.print(jsonObject);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.flush();
                return;
            }


            String examId = request.getParameter("dataId");
            Exam exam = new Exam();
            exam.setId(examId);
            exam.load();


            List<Question> questions = exam.getQuestions();
            if (questions.size() == 0) {
                out.print("[]");
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();
            } else {
                List<DTOQuestion> dtoQuestions = questions.stream().map(question -> new DTOQuestion(question)).collect(Collectors.toList());
                String json = new Gson().toJson(dtoQuestions);
                out.print(json);
                response.setStatus(HttpServletResponse.SC_OK);
                out.flush();
            }
        }catch (Exception e){
            System.out.println(this.getClass()+e.getMessage());
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