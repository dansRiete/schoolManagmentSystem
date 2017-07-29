import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import exceptions.AddingGradeException;
import exceptions.IllegalSubjectTitleException;
import model.Grade;
import model.Subject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import services.GradesInMemoryService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Aleks on 24.07.2017.
 */

@RunWith(DataProviderRunner.class)
public class GradesInMemoryServiceTests {

    private static GradesInMemoryService gradesService = new GradesInMemoryService("grades.json");
    private final static String INIT_FILENAME = "src\\test\\resources\\Grades.json";
    private final static String TEMP_FILENAME = "src\\test\\resources\\tmp.json";
    private final static String GRADES_ON_2017_05_15 =
                    "[Subject: Geographic, Date: 2017-05-15, Mark: 2, " +
                    "Subject: History, Date: 2017-05-15, Mark: 4, " +
                    "Subject: Math, Date: 2017-05-15, Mark: 3]";
    private final static String EMPTY_GRADES = "[]";
    private final static LocalDate DATE_2017_05_15 = LocalDate.of(2017, 5, 15);
    private final static LocalDate ABSENT_DATE = LocalDate.of(2015, 5, 15);

    @BeforeClass
    public static void populate() throws IOException {
        List<Grade> referenceGrades = gradesService.readFromFile(INIT_FILENAME);
        gradesService.setGrades(referenceGrades);
        String actualGradesRepresentation = gradesService.fetchAllGrades().toString();
        System.out.println(actualGradesRepresentation);
        String expectedGradesRepresentation =
                "[Subject: Math, Date: 2017-05-24, Mark: 5, " +
                "Subject: History, Date: 2017-03-12, Mark: 3, " +
                "Subject: Geographic, Date: 2017-04-22, Mark: 9, " +
                "Subject: Geographic, Date: 2017-03-22, Mark: 7, " +
                "Subject: Math, Date: 2017-05-15, Mark: 3, " +
                "Subject: History, Date: 2017-05-15, Mark: 4, " +
                "Subject: Geographic, Date: 2017-01-15, Mark: 3, " +
                "Subject: Geographic, Date: 2017-05-15, Mark: 2, " +
                "Subject: History, Date: 2017-02-20, Mark: 9]";

        assertEquals(expectedGradesRepresentation, actualGradesRepresentation);
    }

    @Test
    public void jsonWriteReadTest() throws IllegalSubjectTitleException, IOException {
        List<Grade> initGrades = new ArrayList<>();
        LocalDate date = LocalDate.now();
        initGrades.add(new Grade(Subject.compose("Subject1"), date, 5));
        initGrades.add(new Grade(Subject.compose("Subject2"), date, 5));
        initGrades.add(new Grade(Subject.compose("Subject3"), date, 5));
        gradesService.writeToFile(TEMP_FILENAME, initGrades);
        List<Grade> deserializedGrades = gradesService.readFromFile(TEMP_FILENAME);
        assertEquals(initGrades, deserializedGrades);
    }

    @Test
    public void jsonWriteReadEmptyTest() throws IllegalSubjectTitleException, IOException {
        List<Grade> initGrades = new ArrayList<>();
        gradesService.writeToFile(TEMP_FILENAME, initGrades);
        List<Grade> deserializedGrades = gradesService.readFromFile(TEMP_FILENAME);
        assertEquals(initGrades, deserializedGrades);
    }

    @DataProvider
    public static Object[][] gradesOnDate(){
        return new Object[][]{
                {DATE_2017_05_15, GRADES_ON_2017_05_15},
                {ABSENT_DATE, EMPTY_GRADES},
                {null, EMPTY_GRADES},
        };
    }

    @Test
    @UseDataProvider("gradesOnDate")
    public void getGradesOnDate(LocalDate date, String result) {
        assertEquals(gradesService.fetchByDate(date).toString(), result);
    }

    @Test
    public void getGradesBySubjectAscendingByDate() {
        Subject GEOGRAPHIC = mock(Subject.class);
        when(GEOGRAPHIC.getTitle()).thenReturn("Geographic");
        String actualGradesRepresentation = gradesService.fetchBySubject(GEOGRAPHIC, true).toString();
        String expectedGradesRepresentation =
                "[Subject: Geographic, Date: 2017-01-15, Mark: 3, " +
                "Subject: Geographic, Date: 2017-03-22, Mark: 7, " +
                "Subject: Geographic, Date: 2017-04-22, Mark: 9, " +
                "Subject: Geographic, Date: 2017-05-15, Mark: 2]";
        assertEquals(expectedGradesRepresentation, actualGradesRepresentation);
    }

    @Test
    public void getGradesBySubjectDescendingByDate() {
        Subject GEOGRAPHIC = mock(Subject.class);
        when(GEOGRAPHIC.getTitle()).thenReturn("Geographic");
        String actualGradesRepresentation = gradesService.fetchBySubject(GEOGRAPHIC, false).toString();
        String expectedGradesRepresentation =
                "[Subject: Geographic, Date: 2017-05-15, Mark: 2, " +
                "Subject: Geographic, Date: 2017-04-22, Mark: 9, " +
                "Subject: Geographic, Date: 2017-03-22, Mark: 7, " +
                "Subject: Geographic, Date: 2017-01-15, Mark: 3]";
        assertEquals(expectedGradesRepresentation, actualGradesRepresentation);
    }

    @DataProvider
    public static Object[][] averageGradesData(){
        return new Object[][]{
                {"History", 5.333333333333333D},
                {"Math", 4D},
                {"Geographic", 5.25D},
        };
    }

    @Test
    @UseDataProvider("averageGradesData")
    public void calculateAverageGradeBySubject(String input, Double expectedValue) throws IllegalSubjectTitleException {
        Subject subject = mock(Subject.class);
        when(subject.getTitle()).thenReturn(input);
        assertThat(gradesService.calculateAvgGrade(subject), is(expectedValue));
    }

    @Test(expected = AddingGradeException.class)
    public void addGradeInPastYearThrowsException() throws IllegalSubjectTitleException, AddingGradeException {
        LocalDate dateInPastYear = LocalDate.of(2016, 1, 1);
        Grade grade = new Grade(mock(Subject.class), dateInPastYear, 3);
        gradesService.addGrade(grade);
    }

    @Test(expected = AddingGradeException.class)
    public void addGradeInTomorowThrowsException() throws IllegalSubjectTitleException, AddingGradeException {
        LocalDate tomorowDate = LocalDate.now().plusDays(1);
        Grade grade = new Grade(mock(Subject.class), tomorowDate, 3);
        gradesService.addGrade(grade);
    }

    @Test(expected = AddingGradeException.class)
    public void addGradeInAlreadyExistException() throws IllegalSubjectTitleException, AddingGradeException {
        LocalDate subjectOnThisDateAlreadyGraded = LocalDate.of(2015, 5, 24);
        Grade grade = new Grade(mock(Subject.class), subjectOnThisDateAlreadyGraded, 3);
        gradesService.addGrade(grade);
    }
}