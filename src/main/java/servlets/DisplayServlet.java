package servlets;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
@WebServlet(urlPatterns = "/display")
public class DisplayServlet extends HttpServlet{

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Grade> grades;
        List<Subject> subjects = new ArrayList<>();
        subjects.add(null);
        subjects.addAll(gradesInMemoryService.fetchAllSubjects());

        if(request.getParameter("selectedSubject") == null || request.getParameter("selectedSubject").equals("")){
            grades = gradesInMemoryService.fetchAllGrades();
        }else {
            long requestedSubjectId = Long.parseLong(request.getParameter("selectedSubject"));
            grades = gradesInMemoryService.fetchBySubject(requestedSubjectId, true);
        }

        request.setAttribute("allGrades", grades);
        request.setAttribute("page", "display");
        request.setAttribute("allSubjects", subjects);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}


