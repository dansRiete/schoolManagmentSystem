package servlets;

import datasources.DataSource;
import model.Grade;
import services.GradesDatabaseService;
import services.GradesInMemoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
@WebServlet(urlPatterns = "/display", loadOnStartup = 1)
public class DisplayServlet extends HttpServlet{

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedSubject = request.getParameter("subject");
        String requestedDate = request.getParameter("date");
        System.out.println("subj: " + requestedSubject + ", date=" + requestedDate);
        List<Grade> grades = null;
        if((requestedSubject == null || requestedSubject.equals("all") || requestedSubject.equals("null")) && (requestedDate == null || requestedDate.equals("all") || requestedDate.equals("null"))){
            grades = gradesInMemoryService.fetchAllGrades();

        }else {
//            grades = gradesInMemoryService.fetchAllGrades();
            grades = gradesInMemoryService.fetchByDate(LocalDate.parse(requestedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        request.setAttribute("allGrades", grades);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}


