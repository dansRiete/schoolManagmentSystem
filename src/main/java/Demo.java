import datasources.DataSource;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import model.Subject;
import services.GradesDatabaseService;
import utils.MainService;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

    public static void main(String[] args) throws AddingGradeException, AddingSubjectException, IOException {
        /*gradesDatabaseService.addGrades(
                Arrays.asList(
                        new Grade(Subject.compose("subj8"), LocalDate.now(), 22),
                        new Grade(Subject.compose("subj9"), LocalDate.now(), 22),
                        new Grade(Subject.compose("subj10"), LocalDate.now(), 22)
                )
        );*/
//        MainService.service.addAllSubjects(Arrays.asList(Subject.compose("First"), Subject.compose("Second"), Subject.compose("Third")));
    }
}
