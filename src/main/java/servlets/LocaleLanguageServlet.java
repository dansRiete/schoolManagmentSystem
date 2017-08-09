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

/**
 * Created by Aleks on 09.08.2017.
 */
@WebServlet(urlPatterns = "/locale")
public class LocaleLanguageServlet extends HttpServlet {

    Logger logger = Logger.getLogger(LocaleLanguageServlet.class);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("locale_language", req.getSession().getAttribute("locale_language") == null ? null : req.getSession().getAttribute("locale_language").toString());
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.println(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Locale has been changed from '" + req.getSession().getAttribute("locale_language") + " on '" + req.getParameter("locale_language") + '\'');
        String language = req.getSession().getAttribute("locale_language") == null ? null : req.getSession().getAttribute("locale_language").toString();
        req.getSession().setAttribute("locale_language", req.getParameter("locale_language"));
        Config.set( req.getSession(), Config.FMT_LOCALE, new java.util.Locale("en_US") );
        resp.sendRedirect(req.getHeader("Referer"));
    }
}
