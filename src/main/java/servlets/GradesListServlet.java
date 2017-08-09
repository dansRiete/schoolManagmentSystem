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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Aleks on 28.07.2017.
 */
@WebServlet(urlPatterns = "/list/grades")
public class GradesListServlet extends HttpServlet {

    GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    Logger logger = Logger.getLogger(GradesListServlet.class);
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

        List<Grade> gradesToDisplay;
        List<Subject> subjects = new ArrayList<>();
        HttpSession httpSession = request.getSession();
        int availablePagesNumber;
        int requestedPageIndex = 0;     //By default zero page is requested

        subjects.add(null);
        subjects.addAll(gradesDatabaseService.fetchAllSubjects());

        String requestSelectedSubject = request.getParameter("selectedSubjectId");
        String requestSelectedDate = request.getParameter("selectedDate");

        String sessionSelectedSubject = httpSession.getAttribute("selectedSubject") == null ? null : httpSession.getAttribute("selectedSubject").toString();
        String sessionSelectedDate = httpSession.getAttribute("selectedDate") == null ? null : httpSession.getAttribute("selectedDate").toString();

        String selectedSubject = requestSelectedSubject == null ? sessionSelectedSubject : requestSelectedSubject;
        String selectedDate = requestSelectedDate == null ? sessionSelectedDate : requestSelectedDate;

        long requestedSubjectId = 0;
        LocalDate requestedDate = null;

        if(selectedSubject != null && !selectedSubject.isEmpty()){
            try {
                requestedSubjectId = Long.parseLong(selectedSubject);
            } catch (NumberFormatException e) {
                logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage() + " " +
                        " Exception during parsing 'requestedSubjectId', requested subject was - '" + selectedSubject + '\'');
            }
        }

        if(selectedDate != null && !selectedDate.isEmpty()) {
            try {
                requestedDate = LocalDate.parse(selectedDate, formatter);
            } catch (DateTimeParseException e) {
                logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage() +
                        " Exception during parsing 'requestedDate', requested subject was - '" + selectedDate + '\'');
            }
        }

        availablePagesNumber = gradesDatabaseService.availablePagesNumber(requestedSubjectId, requestedDate);

        if(request.getParameter("page") != null){
            try {
                requestedPageIndex = Integer.parseInt(request.getParameter("page"));
                if(requestedPageIndex > availablePagesNumber - 1){
                    logger.error("Page " + requestedPageIndex + " is unavailable");
                    requestedPageIndex = 0;
                }
            } catch (NumberFormatException e) {
                logger.error(e.getClass().getCanonicalName() + " " + e.getLocalizedMessage() +
                        " Requested page parameter was '" + request.getParameter("page") + '\'');
            }
        }

        gradesToDisplay = gradesDatabaseService.fetchGrades(requestedSubjectId, requestedDate, requestedPageIndex);

        request.setAttribute("allSubjects", subjects);
        request.setAttribute("selectedSubject", selectedSubject);
        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("paginatorDisplayedPages", paginatorDisplayedPages(availablePagesNumber, requestedPageIndex));
        request.setAttribute("availablePagesNumber", availablePagesNumber);
        request.setAttribute("pageIndex", requestedPageIndex);
        request.setAttribute("allGrades", gradesToDisplay);
        request.setAttribute("pageTitle", "gradesList");

        httpSession.setAttribute("selectedSubject", selectedSubject);
        httpSession.setAttribute("selectedDate", selectedDate);

        request.getRequestDispatcher("/gradesList.jsp").forward(request, response);
    }

}


