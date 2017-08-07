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
 * Created by Aleks on 02.08.2017.
 */

@WebServlet(urlPatterns = "/deleteSubject")
public class DeleteSubjectServlet extends HttpServlet {

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long deletedSubjectId = Long.valueOf(request.getParameter("deletedSubjectId"));
        gradesInMemoryService.forceDeleteSubject(deletedSubjectId);
        response.sendRedirect("/list/subjects");

    }

}
