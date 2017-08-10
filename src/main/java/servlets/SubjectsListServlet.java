package servlets;

import datasources.DataSource;
import model.Subject;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;
import utils.MainService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static utils.Consts.PAGE_TITLE_PARAM_KEY;
import static utils.Consts.SUBJECTS_PARAM_KEY;

/**
 * Created by Aleks on 02.08.2017.
 */
@WebServlet(urlPatterns = "/list/subjects")
public class SubjectsListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Subject> subjects = MainService.service.fetchAllSubjects();
        request.setAttribute(SUBJECTS_PARAM_KEY, subjects);
        request.setAttribute(PAGE_TITLE_PARAM_KEY, "subjectsList");
        request.getRequestDispatcher("/subjectsList.jsp").forward(request, response);
    }
}
