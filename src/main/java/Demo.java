import datasources.DataSource;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import exceptions.SubjectIllegalTitleException;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;
import services.BaseGradesService;
import utils.ServiceFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

    public static void poupulateWithRandomValues(BaseGradesService _service) throws SubjectIllegalTitleException {
        BaseGradesService service = _service;
        Random random = new Random();
        Logger logger = Logger.getLogger(Demo.class);
        List<Subject> subjects = Arrays.asList(
                Subject.compose("Science"),
                Subject.compose("Art"),
                Subject.compose("English"),
                Subject.compose("Geography"),
                Subject.compose("Physics"),
                Subject.compose("Astronomy")
        );
        service.addAllSubjects(subjects);
        subjects = service.fetchAllSubjects();
        List<Grade> gradesToAdd = new ArrayList<>();
        for(int i = 0; i < 300; ){
            try {
                service.addGrade(new Grade(subjects.get(random.nextInt(6)), LocalDate.of(2017, random.nextInt(7)+1, random.nextInt(27)+1), random.nextInt(9)+1));
                i++;
            } catch (AddingGradeException e) {
                logger.info(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws AddingGradeException, AddingSubjectException, IOException {
        ServiceFactory.getService().forceDeleteAllSubjects();
        poupulateWithRandomValues(ServiceFactory.getService());
    }
}
