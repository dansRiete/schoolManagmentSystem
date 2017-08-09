package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.javaws.Globals;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static utils.Consts.LOCALE_PARAM_KEY;

/**
 * Created by Aleks on 09.08.2017.
 */
@WebServlet(urlPatterns = "/locale")
public class LocaleLanguageServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(LocaleLanguageServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> result = new HashMap<>();
        String sessionLocale = (String) request.getSession().getAttribute(LOCALE_PARAM_KEY);
        result.put(LOCALE_PARAM_KEY, sessionLocale);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionLocale = (String) request.getSession().getAttribute(LOCALE_PARAM_KEY);
        String requestLocale = request.getParameter(LOCALE_PARAM_KEY);
        logger.info("Locale has been changed from '" + sessionLocale + " on '" + requestLocale + '\'');
        request.getSession().setAttribute(LOCALE_PARAM_KEY, requestLocale);
        response.sendRedirect(request.getHeader("Referer"));
    }
}
