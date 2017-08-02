package servlets;

import datasources.DataSource;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Aleks on 29.07.2017.
 */
@WebServlet(urlPatterns = "/deleteGrade")
public class DeleteGradeServlet extends HttpServlet {

    GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long deletedGradeId = Long.valueOf(request.getParameter("deletedGradeId"));
        gradesInMemoryService.deleteGrade(deletedGradeId);
        System.out.println("request.getParameter(\"selectedSubject\") = " + request.getParameter("selectedSubject"));
        request.setAttribute("selectedSubject", request.getParameter("selectedSubject"));
        request.setAttribute("selectedDate", request.getParameter("selectedDate"));
        response.sendRedirect("/display");
    }

}
