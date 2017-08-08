import datasources.DataSource;
import datasources.DataSourceH2Test;
import datasources.DataSourceTest;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import exceptions.DeletingSubjectException;
import model.Grade;
import model.Subject;
import services.BaseGradesService;
import services.GradesDatabaseService;
import services.GradesInMemoryService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

    public static void main(String[] args) throws AddingGradeException, AddingSubjectException, IOException {
        GradesDatabaseService gradesDatabaseService = new GradesDatabaseService(DataSource.getSqlSessionFactory());
        gradesDatabaseService.forceDeleteAllSubjects();
        String content = new Scanner(new File("grades.json")).useDelimiter("\\Z").next();
        gradesDatabaseService.fromJson(content);
        System.out.println(gradesDatabaseService.fetchAllGrades());
    }
}
