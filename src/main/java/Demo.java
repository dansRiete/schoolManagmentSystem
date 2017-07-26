import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import exceptions.IllegalTitleException;
import model.Grade;
import model.Subject;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {


    private static final String FILENAME = "grades.json";

    public static void main(String[] args) throws AddingGradeException {

        Subject MATH = null;
        Subject HISTORY = null;
        Subject GEOGRAPHIC = null;
        Subject WITHSPACES = null;
        try {
            MATH = Subject.compose("Math");
            HISTORY = Subject.compose("History");
            GEOGRAPHIC = Subject.compose("Geographic");
//            WITHSPACES = Subject.compose(" ");    //Exception
//            WITHSPACES = Subject.compose("Geog raphic");    //Exception
        } catch (IllegalTitleException e) {
            e.printStackTrace();
        }


        System.out.println(new SubjectDao().get(1L));
        System.out.println(new GradeDao().get(1L));

        GradesService gradesService = new GradesService();

        try {
            gradesService.setGrades(gradesService.readFromFile(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("All elements: " + gradesService.getGrades());
        System.out.println("On date 2017/5/15 " + gradesService.getGrades(LocalDate.of(2017, 5, 15)));
        System.out.println("On subject geographic, ascending " + gradesService.getGrades(GEOGRAPHIC, true));
        System.out.println("On subject geographic, descending " + gradesService.getGrades(GEOGRAPHIC, false));
        System.out.println("Average on History = " + gradesService.calculateAvgGrade(HISTORY));
        System.out.println("Average on Math = " + gradesService.calculateAvgGrade(MATH));
        System.out.println("Average on Geographic = " + gradesService.calculateAvgGrade(GEOGRAPHIC));

//        gradesService.addGrade(MATH, LocalDate.of(2016, 5, 24), 5);    //Exception
//        gradesService.addGrade(MATH, LocalDate.of(2017, 12, 25), 5);    //Exception
        gradesService.addGrade(MATH, LocalDate.of(2017, 7, 24), 5);

        /*try {
            gradesService.writeToFile(FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}
