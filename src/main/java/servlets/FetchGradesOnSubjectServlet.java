package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import model.Grade;
import model.Subject;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleks on 03.08.2017.
 */
@WebServlet(urlPatterns = "/fetchBySubject")
public class FetchGradesOnSubjectServlet extends HttpServlet {

    private GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        String selectedSubjectIdParameter = request.getParameter("selectedSubjectIdParameter");
        System.out.println("selectedSubjectIdParameter=" + selectedSubjectIdParameter);
        long selectedSubjectId = Long.parseLong(selectedSubjectIdParameter);
        List<Grade> subjects = gradesDatabaseService.fetchGrades(selectedSubjectId, null);
        System.out.println("subjects.size() = " + subjects.size());
        result.put("gradesOnSubjectCount", subjects.size());
        result.put("subjectId", subjects.size());
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(result));
    }
}
