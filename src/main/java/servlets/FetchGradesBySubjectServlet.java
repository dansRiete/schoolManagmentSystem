package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;
import services.GradesInMemoryService;
import utils.MainService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Consts.SELECTED_DATE_PARAM_KEY;
import static utils.Consts.SELECTED_SUBJECT_PARAM_KEY;

/**
 * Created by Aleks on 03.08.2017.
 */
@WebServlet(urlPatterns = "/fetchBySubject")
public class FetchGradesBySubjectServlet extends HttpServlet {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Logger logger = Logger.getLogger(FetchGradesBySubjectServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        logger.debug("request.getParameter(SELECTED_SUBJECT_PARAM_KEY) = " + request.getParameter(SELECTED_SUBJECT_PARAM_KEY));
        long requestedSubjectId = Long.parseLong(request.getParameter(SELECTED_SUBJECT_PARAM_KEY));
        List<Grade> subjects = MainService.service.fetchGrades(requestedSubjectId, null);
        result.put("gradesOnSubjectCount", subjects.size());
        result.put("subjectId", subjects.size());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(result));
    }
}
