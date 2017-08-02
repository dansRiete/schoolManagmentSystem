package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import exceptions.NoGradesException;
import services.GradesDatabaseService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleks on 01.08.2017.
 */
@WebServlet("/average")
public class AverageServlet extends HttpServlet {

    private GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DecimalFormat averageGradeDecimalFormat = new DecimalFormat("#.##");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idParameter = request.getParameter("selectedSubjectId");
        String selectedDateParameter = request.getParameter("selectedDate");
        long selectedSubjectId = idParameter == null || idParameter.equals("") ? 0 : Long.parseLong(idParameter);
        LocalDate selectedDate = selectedDateParameter == null || selectedDateParameter.equals("") ? null : LocalDate.parse(selectedDateParameter, formatter);
        String selectedSubjectTitle = selectedSubjectId == 0 ? "all subjects" : gradesDatabaseService.fetchSubject(selectedSubjectId).getTitle();
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("avg", averageGradeDecimalFormat.format(gradesDatabaseService.calculateAvgGrade(selectedSubjectId, selectedDate)));
        } catch (NoGradesException e) {
            result.put("avg", null);
        }

        result.put("subjectTitle", selectedSubjectTitle);
        result.put("selectedDate", selectedDate == null ? "" : selectedDate.toString());
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(result));
    }
}
