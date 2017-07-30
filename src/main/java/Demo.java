import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import services.BaseGradesService;
import services.GradesInMemoryService;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

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
