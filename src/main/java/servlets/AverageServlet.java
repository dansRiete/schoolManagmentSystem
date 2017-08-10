package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import exceptions.NoGradesException;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;
import services.GradesInMemoryService;
import utils.MainService;

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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static utils.Consts.LOCALE_PARAM_KEY;
import static utils.Consts.SELECTED_DATE_PARAM_KEY;
import static utils.Consts.SELECTED_SUBJECT_PARAM_KEY;

/**
 * Created by Aleks on 01.08.2017.
 */
@WebServlet("/average")
public class AverageServlet extends HttpServlet {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DecimalFormat averageGradeDecimalFormat = new DecimalFormat("#.##");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String sessionLocale = (String) request.getSession().getAttribute(LOCALE_PARAM_KEY);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("text", Locale.forLanguageTag(sessionLocale));
        String selecteSubjectIdRawParam = request.getParameter(SELECTED_SUBJECT_PARAM_KEY);
        String selectedDateRawParam = request.getParameter(SELECTED_DATE_PARAM_KEY);
        long selectedSubjectId = selecteSubjectIdRawParam == null || selecteSubjectIdRawParam.equals("") ? 0 : Long.parseLong(selecteSubjectIdRawParam);
        LocalDate selectedDate = selectedDateRawParam == null || selectedDateRawParam.equals("") ? null : LocalDate.parse(selectedDateRawParam, formatter);
        String subject = selectedSubjectId == 0 ? resourceBundle.getString("entity.all_subjects") : MainService.service.fetchSubject(selectedSubjectId).getTitle();
        String date = resourceBundle.getString("entity.date") + ": " + (selectedDate == null ? resourceBundle.getString("entity.all_dates") : selectedDate.toString());
        StringBuilder modalTitle = new StringBuilder(resourceBundle.getString("info.avg_mark_on"))
                .append(": ")
                .append(subject)
                .append(", ")
                .append(" ")
                .append(date);

        Map<String, Object> result = new HashMap<>();

        try {
            double averageMark = MainService.service.calculateAvgGrade(selectedSubjectId, selectedDate);
            result.put("modal_title", modalTitle);
            result.put("modal_body", averageGradeDecimalFormat.format(averageMark));
        } catch (NoGradesException e) {
            result.put("modal_title", modalTitle);
            result.put("modal_body", resourceBundle.getString("result.no_grades_found"));
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(result));
    }
}
