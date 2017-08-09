package servlets;

import datasources.DataSource;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static utils.Consts.SELECTED_DATE_PARAM_KEY;
import static utils.Consts.SELECTED_SUBJECT_PARAM_KEY;

/**
 * Created by Aleks on 29.07.2017.
 */
@WebServlet(urlPatterns = "/deleteGrade")
public class DeleteGradeServlet extends HttpServlet {

    private GradesDatabaseService gradesInMemoryService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long deletedGradeId = Long.valueOf(request.getParameter("deletedGradeId"));
        gradesInMemoryService.deleteGrade(deletedGradeId);
        request.setAttribute(SELECTED_SUBJECT_PARAM_KEY, request.getParameter(SELECTED_SUBJECT_PARAM_KEY));
        request.setAttribute(SELECTED_DATE_PARAM_KEY, request.getParameter(SELECTED_DATE_PARAM_KEY));
        response.sendRedirect("/list/grades");
    }

}
