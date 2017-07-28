package servlets;

import datasources.DataSource;
import services.GradesDatabaseService;
import services.GradesInMemoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aleks on 28.07.2017.
 */
@WebServlet(urlPatterns = "/start")
public class Start extends HttpServlet{

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("allGrades", gradesInMemoryService.fetchAllGrades());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}
