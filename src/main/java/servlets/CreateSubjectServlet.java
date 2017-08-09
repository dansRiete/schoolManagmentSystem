package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import exceptions.AddingSubjectException;
import model.Subject;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static utils.Consts.PAGE_TITLE_PARAM_KEY;

/**
 * Created by Aleks on 31.07.2017.
 */

@WebServlet(urlPatterns = "/create/subject")
public class CreateSubjectServlet extends HttpServlet {
    Logger logger = Logger.getLogger(CreateGradeServlet.class);
    GradesDatabaseService gradesService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(PAGE_TITLE_PARAM_KEY, "createSubject");
        req.getRequestDispatcher("/createSubject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//todo and up
//        req.setAttribute("page", "createSubject");
        String subjectTitle = req.getParameter("newSubjectTitle");
        Subject addedSubject = null;
        Map<String, Object> result = new HashMap<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            addedSubject = Subject.compose(subjectTitle);
            gradesService.addSubject(addedSubject.getTitle());
            result.put("status", "success");
            result.put("message", "Subject has been successfuly added");
            req.setAttribute("message", "Success: subject \"" + addedSubject +
                    "\" has been successfully created");
        }catch (AddingSubjectException e){
            logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
            req.setAttribute("message", "Error: " + e.getLocalizedMessage());
            result.put("status", "error");
            result.put("message", e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
            req.setAttribute("subjectTitle", subjectTitle);
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.println(gson.toJson(result));
    }
}
