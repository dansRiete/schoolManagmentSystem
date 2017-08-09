package servlets;

import org.apache.log4j.Logger;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.Consts.PASSWORD_PARAM_KEY;
import static utils.Consts.USERNAME_PARAM_KEY;

/**
 * Created by Aleks on 03.08.2017.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();
    Logger logger = Logger.getLogger(LoginServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter(USERNAME_PARAM_KEY);
        String password = request.getParameter(PASSWORD_PARAM_KEY);

            if (userService.isExists(username, password)) {
                request.getSession().setAttribute(USERNAME_PARAM_KEY, username);
                logger.info("User " + username + " is logged in");
                response.sendRedirect("/list/grades");
                return;
            } else {
                //todo log
            }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

}
