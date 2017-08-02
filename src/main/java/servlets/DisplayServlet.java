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
import javax.servlet.http.HttpSession;
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

        List<Grade> grades = null;
        List<Subject> subjects = new ArrayList<>();
        subjects.add(null);
        subjects.addAll(gradesInMemoryService.fetchAllSubjects());
        request.setAttribute("allSubjects", subjects);
        HttpSession httpSession = request.getSession();

        String selectedSubjectRaw = request.getParameter("selectedSubjectId");
        String selectedDateRaw = request.getParameter("selectedDate");

        String selectedSubjectSession = httpSession.getAttribute("selectedSubject") == null ? null : httpSession.getAttribute("selectedSubject").toString();
        String selectedDateSession = httpSession.getAttribute("selectedDate") == null ? null : httpSession.getAttribute("selectedDate").toString();

        String selectedSubject = selectedSubjectRaw == null || selectedSubjectRaw.equals("") ? selectedSubjectSession : selectedSubjectRaw;
        String selectedDate = selectedDateRaw == null || selectedDateRaw.equals("") ? selectedDateSession : selectedDateRaw;


        if((selectedSubject == null || selectedSubject.equals("")) && (selectedDate == null || selectedDate.equals(""))){
            grades = gradesInMemoryService.fetchAllGrades();
        }else if((selectedSubject != null && !selectedSubject.equals("")) && (selectedDate == null || selectedDate.equals(""))){
            long requestedSubjectId = Long.parseLong(selectedSubject);
            grades = gradesInMemoryService.fetchBySubject(requestedSubjectId, true);
            request.setAttribute("selectedSubject", selectedSubject);
            httpSession.setAttribute("selectedSubject", selectedSubject);
        }else if((selectedSubject == null || selectedSubject.equals("")) && (selectedDate != null && !selectedDate.equals(""))){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate requestedDate = LocalDate.parse(selectedDate, formatter);
            request.setAttribute("selectedDate", selectedDate);
            httpSession.setAttribute("selectedDate", selectedDate);
            grades = gradesInMemoryService.fetchByDate(requestedDate);
        }else {
            long requestedSubjectId = Long.parseLong(selectedSubject);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate requestedDate = LocalDate.parse(selectedDate, formatter);
            request.setAttribute("selectedSubject", selectedSubject);
            httpSession.setAttribute("selectedSubject", selectedSubject);
            request.setAttribute("selectedDate", selectedDate);
            httpSession.setAttribute("selectedDate", selectedDate);
            grades = gradesInMemoryService.fetchBySubjectAndDate(requestedSubjectId, requestedDate);
        }

        request.setAttribute("allGrades", grades);
        request.setAttribute("page", "display");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}


