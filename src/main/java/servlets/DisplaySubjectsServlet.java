package servlets;

import datasources.DataSource;
import model.Subject;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Aleks on 02.08.2017.
 */
@WebServlet(urlPatterns = "/display/subjects")
public class DisplaySubjectsServlet extends HttpServlet {


    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Subject> subjects = gradesInMemoryService.fetchAllSubjects();
        request.setAttribute("subjects", subjects);
        request.setAttribute("page", "displaySubjects");
        request.getRequestDispatcher("/displaySubjects.jsp").forward(request, response);
    }
}
