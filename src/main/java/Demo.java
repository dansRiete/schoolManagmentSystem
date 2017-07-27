import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import services.GradesService;
import services.MyBatisService;

import java.time.LocalDate;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Demo {

//    private static final String FILENAME = "grades.json";

    public static void main(String[] args) throws AddingGradeException {

        SubjectDao subjectDao = new SubjectDao(MyBatisService.getSqlSessionFactory());
        GradeDao gradeDao = new GradeDao(MyBatisService.getSqlSessionFactory());

        /*Subject subject = subjectDao.getAll().get(1);
        Random random = new Random();
        Grade someGrade = null;
        gradeDao.create(
                Arrays.asList(
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        someGrade = new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now().withDayOfMonth(random.nextInt(30)), random.nextInt(10)),
                        new Grade(subject, LocalDate.now().withDayOfMonth(random.nextInt(30)), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now(), random.nextInt(10)),
                        new Grade(subject, LocalDate.now().withDayOfMonth(random.nextInt(30)), random.nextInt(10)),
                        new Grade(subject, LocalDate.now().withDayOfMonth(random.nextInt(30)), random.nextInt(10)),
                        new Grade(subject, LocalDate.now().withDayOfMonth(random.nextInt(30)), random.nextInt(10))
                ));*/

//        Subject neededSubject = subjectDao.getAll().get(1);
//        System.out.println("Need subj: " + neededSubject);
//        System.out.println(gradeDao.getOnSubject(neededSubject));
        GradesService gradesService = new GradesService();
        Subject subject = subjectDao.getAll().get(1);
        gradesService.addGradeToDb(new Grade(subject, LocalDate.now().minusDays(3), 3));
        gradesService.addGradeToDb(new Grade(subject, LocalDate.now().minusDays(5), 3));

    }
}
