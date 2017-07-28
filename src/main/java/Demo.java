import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import exceptions.IllegalSubjectTitleException;
import jdk.internal.dynalink.linker.LinkerServices;
import model.Grade;
import model.Subject;
import services.BaseGradesService;
import services.GradesInMemoryService;
import services.GradesService;
import datasources.DataSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

//    private static final String FILENAME = "grades.json";

    public static void main(String[] args) throws AddingGradeException {

        GradesInMemoryService gradesInMemoryService = new GradesInMemoryService("grades.json");

        System.out.println(gradesInMemoryService.fetchAllGrades());
        List<Subject> subjects = gradesInMemoryService.fetchAllSubjects();
        System.out.println(subjects);

        Grade addedGrade = new Grade(subjects.get(0), LocalDate.now(), 5);
        System.out.println("Added grade = " + addedGrade);
//        gradesInMemoryService.addGrade(addedGrade);

        System.out.println(BaseGradesService.represent(gradesInMemoryService.fetchAllGrades()));
    }
}
