import dao.GradeDao;
import dao.SubjectDao;
import exceptions.IllegalTitleException;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.junit.BeforeClass;
import org.junit.Test;
import services.MyBatisTestService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public class GradesDbTests {

    private static GradeDao gradeDao = new GradeDao(MyBatisTestService.getSqlSessionFactory());
    private static SubjectDao subjectDao = new SubjectDao(MyBatisTestService.getSqlSessionFactory());
    private static List<Subject> initSubjects;
    private static List<Grade> initGrades;

    @BeforeClass
    public static void populate() throws IllegalTitleException {
        initSubjects = Arrays.asList(
                Subject.compose("Math"), Subject.compose("Geographic"), Subject.compose("History")
        );
        subjectDao.create(initSubjects);
        initSubjects = subjectDao.getAll();
        initGrades = Arrays.asList(
                new Grade(initSubjects.get(0), LocalDate.now(), 5),
                new Grade(initSubjects.get(1), LocalDate.now().minusDays(1), 8)
        );
        gradeDao.create(initGrades);
    }

    @Test
    public void test(){}
}
