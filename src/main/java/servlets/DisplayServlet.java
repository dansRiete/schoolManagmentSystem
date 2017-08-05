package servlets;

import datasources.DataSource;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;
import services.GradesDatabaseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
@WebServlet(urlPatterns = "/display")
public class DisplayServlet extends HttpServlet {

    GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    Logger logger = Logger.getLogger(DisplayServlet.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private int[] paginatorDisplayedPages(int availablePagesNumber, int activePageIndex) {

        int[] paginatorPages = null;

        if (availablePagesNumber <= 7) {
            paginatorPages = new int[]{0, 1, 2, 3, 4, 5, 6};
        } else {

            if (activePageIndex <= 3) {
                paginatorPages = new int[]{0, 1, 2, 3, 4, 5, availablePagesNumber - 1};
            } else if (activePageIndex > 3 && activePageIndex <= availablePagesNumber - 4) {
                paginatorPages = new int[]{
                        0,
                        activePageIndex - 2,
                        activePageIndex - 1,
                        activePageIndex,
                        activePageIndex + 1,
                        activePageIndex + 2,
                        availablePagesNumber - 1
                };
            } else if (activePageIndex > 3 && activePageIndex > availablePagesNumber - 4) {
                paginatorPages = new int[]{
                        0,
                        availablePagesNumber - 6,
                        availablePagesNumber - 5,
                        availablePagesNumber - 4,
                        availablePagesNumber - 3,
                        availablePagesNumber - 2,
                        availablePagesNumber - 1
                };
            }
        }
        return paginatorPages;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Grade> grades = null;
        List<Subject> subjects = new ArrayList<>();
        HttpSession httpSession = request.getSession();
        int availablePagesNumber;
        int requestedPageIndex = 0;     //Default 0
        try {
            requestedPageIndex = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage() + '\n' + "pageIndex = " + request.getParameter("pageIndex"));
        }

        subjects.add(null);
        subjects.addAll(gradesDatabaseService.fetchAllSubjects());
        request.setAttribute("allSubjects", subjects);

        String requestSelectedSubject = request.getParameter("selectedSubjectId");
        String requestSelectedDate = request.getParameter("selectedDate");

        String sessionSelectedSubject = httpSession.getAttribute("selectedSubject") == null ? null : httpSession.getAttribute("selectedSubject").toString();
        String sessionSelectedDate = httpSession.getAttribute("selectedDate") == null ? null : httpSession.getAttribute("selectedDate").toString();

        String selectedSubject = requestSelectedSubject == null ? sessionSelectedSubject : requestSelectedSubject;
        String selectedDate = requestSelectedDate == null ? sessionSelectedDate : requestSelectedDate;


        long requestedSubjectId;
        LocalDate requestedDate;

        try {
            requestedSubjectId = Long.parseLong(selectedSubject);
        } catch (NumberFormatException ne) {
            logger.error(ne.getMessage() + "\nException during parsing 'requestedSubjectId', requested subject was - '" + selectedSubject + '\'');
            requestedSubjectId = 0;
        }

        try {
            requestedDate = LocalDate.parse(selectedDate, formatter);
        } catch (DateTimeParseException de) {
            logger.error(de.getMessage() + "\nException during parsing 'requestedDate', requested subject was - '" + selectedDate);
            requestedDate = null;
        }

        availablePagesNumber = gradesDatabaseService.availablePagesNumber(requestedSubjectId, requestedDate);

        request.setAttribute("selectedSubject", selectedSubject);
        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("paginatorDisplayedPages", paginatorDisplayedPages(availablePagesNumber, requestedPageIndex));
        request.setAttribute("availablePagesNumber", availablePagesNumber);
        request.setAttribute("pageIndex", requestedPageIndex);

        httpSession.setAttribute("selectedSubject", selectedSubject);
        httpSession.setAttribute("selectedDate", selectedDate);

        grades = gradesDatabaseService.fetchGrades(requestedSubjectId, requestedDate, requestedPageIndex);

        request.setAttribute("allGrades", grades);
//        request.setAttribute("page", "display");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}


