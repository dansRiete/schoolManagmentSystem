import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import exceptions.IllegalTitleException;
import model.Grade;
import model.Subject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

    private static final String FILENAME = "grades.json";

    public static void main(String[] args) throws AddingGradeException {

        SubjectDao subjectDao = new SubjectDao();
        GradeDao gradeDao = new GradeDao();

        Grade grade = gradeDao.getAll().get(0);
        grade.getSubject().setTitle("Changed");
        gradeDao.update(grade);

        /*Subject subject = subjectDao.getAll().get(1);
        Random random = new Random();
        gradeDao.create(
                Arrays.asList(
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10))
                ));*/
    }
}
