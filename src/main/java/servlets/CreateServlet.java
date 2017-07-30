package servlets;

import datasources.DataSource;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by Aleks on 29.07.2017.
 */

@WebServlet(urlPatterns = "/create")
public class CreateServlet extends HttpServlet {

    Logger logger = Logger.getLogger(CreateServlet.class);
    GradesDatabaseService gradesService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("subjects", gradesService.fetchAllSubjects());
        req.getRequestDispatcher("/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long selectedSubjectId = null;
        Integer mark = null;
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            selectedSubjectId = Long.valueOf(req.getParameter("selectedSubject"));
            mark = Integer.valueOf(req.getParameter("mark"));
            date = LocalDate.parse(req.getParameter("date"), formatter);
            Subject subject = gradesService.fetchSubject(selectedSubjectId);
            gradesService.addGrade(new Grade(subject, date, mark));
            resp.sendRedirect("/display");
        }catch (NumberFormatException e){
            logger.error(e.getMessage());
            req.setAttribute("message", "Error: " + e.getLocalizedMessage());
            req.setAttribute("selectedSubjectId", selectedSubjectId);
            req.setAttribute("date", req.getParameter("date"));
            req.setAttribute("mark", req.getParameter("mark"));
            req.setAttribute("subjects", gradesService.fetchAllSubjects());
            req.getRequestDispatcher("/create.jsp").forward(req, resp);
        }catch (AddingGradeException e) {
            logger.error(e.getMessage());
            req.setAttribute("message", "Error: " + e.getLocalizedMessage());
            req.setAttribute("selectedSubjectId", selectedSubjectId);
            req.setAttribute("date", date);
            req.setAttribute("mark", mark);
            req.setAttribute("subjects", gradesService.fetchAllSubjects());
            req.getRequestDispatcher("/create.jsp").forward(req, resp);
        }catch (DateTimeParseException e) {
            logger.error(e.getMessage());
            req.setAttribute("message", "Enter a date");
            req.setAttribute("selectedSubjectId", selectedSubjectId);
            req.setAttribute("date", date);
            req.setAttribute("mark", mark);
            req.setAttribute("subjects", gradesService.fetchAllSubjects());
            req.getRequestDispatcher("/create.jsp").forward(req, resp);
        }
    }
}
