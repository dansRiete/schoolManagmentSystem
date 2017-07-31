package servlets;

import datasources.DataSource;
import exceptions.AddingGradeException;
import exceptions.IllegalSubjectTitleException;
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
 * Created by Aleks on 31.07.2017.
 */

@WebServlet(urlPatterns = "/create/subject")
public class CreateSubjectServlet extends HttpServlet {
    Logger logger = Logger.getLogger(CreateGradeServlet.class);
    GradesDatabaseService gradesService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("page", "createSubject");
        req.getRequestDispatcher("/createSubject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String subjectTitle = req.getParameter("subjectTitle");
        Subject addedSubject = null;

        try {
            addedSubject = Subject.compose(subjectTitle);
            gradesService.addSubject(addedSubject);
            req.setAttribute("message", "Subject \"" + addedSubject + "\" has been successfully created");
            req.getRequestDispatcher("/createSubject.jsp").forward(req, resp);
        }catch (IllegalSubjectTitleException e){
            logger.error(e.getMessage());
            req.setAttribute("message", "Error: " + e.getLocalizedMessage());
            req.setAttribute("subjectTitle", subjectTitle);
            req.getRequestDispatcher("/createSubject.jsp").forward(req, resp);
        }
    }
}
