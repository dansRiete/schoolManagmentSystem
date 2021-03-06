package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;
import services.BaseGradesService;
import utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleks on 29.07.2017.
 */

@WebServlet(urlPatterns = "/create/grade")
public class CreateGradeServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(CreateGradeServlet.class);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private BaseGradesService service = ServiceFactory.getService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("subjects", service.fetchAllSubjects());
        req.setAttribute("pageTitle", "createGrade");

        Long selectedSubjectId;
        Integer mark;
        LocalDate date;

        Grade addedGrade;
        Map<String, Object> result = new HashMap<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            selectedSubjectId = Long.valueOf(req.getParameter("modalSelectedSubjectId"));
            mark = Integer.valueOf(req.getParameter("mark"));
            date = LocalDate.parse(req.getParameter("date"), dateFormatter);
            Subject subject = service.fetchSubject(selectedSubjectId);
            addedGrade = new Grade(subject, date, mark);
            service.addGrade(addedGrade);
            result.put("statusMessage", "Success: Grade has been successfully created");
        }catch (NumberFormatException e){
            logger.error("Add grade error " + e.getMessage());
            result.put("statusMessage", "Error: " + e.getLocalizedMessage());
        }catch (AddingGradeException e) {
            logger.error("Add grade error " + e.getMessage());
            result.put("statusMessage", "Error: " + e.getLocalizedMessage());
        }catch (DateTimeParseException e) {
            logger.error("Add grade error " + e.getMessage());
            result.put("statusMessage", "Error: Enter a date");
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.println(gson.toJson(result));
    }
}
