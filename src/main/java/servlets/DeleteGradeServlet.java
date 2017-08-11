package servlets;

import datasources.DataSource;
import exceptions.DeletingSubjectException;
import org.apache.log4j.Logger;
import services.BaseGradesService;
import services.GradesDatabaseService;
import utils.ServiceFactory;

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

    private BaseGradesService service = ServiceFactory.getService();
    private Logger logger = Logger.getLogger(DeleteGradeServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long deletedGradeId = Long.valueOf(request.getParameter("deletedGradeId"));
        try {
            service.deleteGrade(deletedGradeId);
            request.setAttribute(SELECTED_SUBJECT_PARAM_KEY, request.getParameter(SELECTED_SUBJECT_PARAM_KEY));
            request.setAttribute(SELECTED_DATE_PARAM_KEY, request.getParameter(SELECTED_DATE_PARAM_KEY));
        } catch (DeletingSubjectException e) {
            logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
        }

        response.sendRedirect("/list/grades");
    }

}
