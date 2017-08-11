package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datasources.DataSource;
import exceptions.AddingSubjectException;
import exceptions.SubjectExistsException;
import exceptions.SubjectIllegalTitleException;
import model.Subject;
import org.apache.log4j.Logger;
import services.BaseGradesService;
import utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static utils.Consts.LOCALE_PARAM_KEY;
import static utils.Consts.PAGE_TITLE_PARAM_KEY;

/**
 * Created by Aleks on 31.07.2017.
 */

@WebServlet(urlPatterns = "/create/subject")
public class CreateSubjectServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(CreateGradeServlet.class);
    private BaseGradesService service = ServiceFactory.getService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(PAGE_TITLE_PARAM_KEY, "createSubject");
        req.getRequestDispatcher("/createSubject.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String subjectTitle = req.getParameter("newSubjectTitle");
        Subject addedSubject;
        Map<String, Object> result = new HashMap<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String sessionLocale = (String) req.getSession().getAttribute(LOCALE_PARAM_KEY);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("text", Locale.forLanguageTag(sessionLocale));

        try {
            addedSubject = Subject.compose(subjectTitle);
            service.addSubject(addedSubject.getTitle());
            result.put("status", "success");
            result.put("message", "Subject has been successfuly added");
            req.setAttribute("message", "Success: subject \"" + addedSubject +
                    "\" has been successfully created");
        }catch (SubjectIllegalTitleException e){
            logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
            req.setAttribute("message", resourceBundle.getString("error.illegal_subject_title"));
            result.put("status", "error");
            result.put("message", resourceBundle.getString("error.illegal_subject_title"));
            req.setAttribute("subjectTitle", subjectTitle);
        } catch (SubjectExistsException e) {
            logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
            req.setAttribute("message", resourceBundle.getString("error.subject_exists"));
            result.put("status", "error");
            result.put("message", resourceBundle.getString("error.subject_exists"));
            req.setAttribute("subjectTitle", subjectTitle);
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println(gson.toJson(result));
    }
}
