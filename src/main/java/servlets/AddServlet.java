package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Aleks on 29.07.2017.
 */
@WebServlet(urlPatterns = "/addServlet")
public class AddServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Subject: " + req.getParameter("subject") + ", Mark: " + req.getParameter("mark"));
        resp.sendRedirect("/table");
    }
}
