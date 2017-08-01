package servlets;

import datasources.DataSource;
import services.GradesDatabaseService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aleks on 01.08.2017.
 */
@WebServlet("/average")
public class AverageServlet extends HttpServlet {
    GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getParameter("selectedSubject"));
    }
}
