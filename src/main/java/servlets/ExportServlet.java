package servlets;

import datasources.DataSource;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static utils.Consts.PAGE_TITLE_PARAM_KEY;

/**
 * Created by Aleks on 08.08.2017.
 */
@WebServlet(urlPatterns = "/export")
public class ExportServlet extends HttpServlet{

    private GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(PAGE_TITLE_PARAM_KEY, "export");
        req.getRequestDispatcher("/export.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition","attachment;filename=grades.json");

        InputStream in = new ByteArrayInputStream(gradesDatabaseService.toJson().getBytes("UTF-8"));
        ServletOutputStream out = resp.getOutputStream();

        byte[] outputByte = new byte[4096];

        while(in.read(outputByte, 0, 4096) != -1) {
            out.write(outputByte, 0, 4096);
        }

        in.close();
        out.flush();
        out.close();
    }
}
