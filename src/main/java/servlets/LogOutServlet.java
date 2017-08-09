package servlets;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.Consts.USERNAME_PARAM_KEY;

/**
 * Created by Aleks on 03.08.2017.
 */
@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("User " + request.getSession().getAttribute(USERNAME_PARAM_KEY) + " is logged out");
        request.getSession().setAttribute(USERNAME_PARAM_KEY, null);
        response.sendRedirect("/login.jsp");
    }
}
