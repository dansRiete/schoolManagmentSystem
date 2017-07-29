package servlets;

import datasources.DataSource;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aleks on 29.07.2017.
 */
@WebServlet(urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Delete ID: " + request.getParameter("deleteId"));
        response.sendRedirect("/table");
    }

}
