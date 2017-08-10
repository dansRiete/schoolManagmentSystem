package servlets;

import datasources.DataSource;
import org.apache.commons.io.IOUtils;
import services.GradesDatabaseService;
import services.GradesInMemoryService;
import utils.MainService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static utils.Consts.PAGE_TITLE_PARAM_KEY;

/**
 * Created by Aleks on 08.08.2017.
 */
@WebServlet(urlPatterns = "/import")
@MultipartConfig
public class ImportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(PAGE_TITLE_PARAM_KEY, "import");
        req.getRequestDispatcher("/import.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(fileContent, writer, "UTF-8");
        String importedJson = writer.toString();
        MainService.service.reloadCollectionFromJson(importedJson);
        resp.sendRedirect("/list/grades");
    }
}
