import dao.GradeDao;
import dao.SubjectDao;
import exceptions.IllegalSubjectTitleException;
import model.Grade;
import model.Subject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import datasources.DataSourceTest;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Aleks on 26.07.2017.
 */

@SuppressWarnings("Duplicates")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GradesDatabaseServiceTests {

    private static GradeDao gradeDao = new GradeDao(DataSourceTest.getSqlSessionFactory());
    private static SubjectDao subjectDao = new SubjectDao(DataSourceTest.getSqlSessionFactory());
    private static List<Subject> initSubjects;
    private static List<Grade> initGrades;
    private final static Comparator<Grade> gradeByIdComparator = Comparator.comparing(Grade::getId);

    @BeforeClass
    public static void populate() throws IllegalSubjectTitleException {
        gradeDao.deleteAll();
        subjectDao.deleteAll();
        initSubjects = Arrays.asList(
                Subject.compose("Math"), Subject.compose("Geographic"), Subject.compose("History")
        );
        subjectDao.createAll(initSubjects);
        initSubjects = subjectDao.getAll();
        initGrades = Arrays.asList(
                new Grade(initSubjects.get(0), LocalDate.of(2017, 5, 24), 5),
                new Grade(initSubjects.get(2), LocalDate.of(2017, 3, 12), 3),
                new Grade(initSubjects.get(1), LocalDate.of(2017, 4, 22), 9),
                new Grade(initSubjects.get(1), LocalDate.of(2017, 3, 22), 7),
                new Grade(initSubjects.get(0), LocalDate.of(2017, 5, 15), 3),
                new Grade(initSubjects.get(2), LocalDate.of(2017, 5, 15), 4),
                new Grade(initSubjects.get(1), LocalDate.of(2017, 1, 15), 3),
                new Grade(initSubjects.get(1), LocalDate.of(2017, 5, 15), 2),
                new Grade(initSubjects.get(2), LocalDate.of(2017, 2, 20), 9)
        );
        gradeDao.createAll(initGrades);
        initGrades = gradeDao.getAll();
//        List<Grade> receivedGrades = gradeDao.getAll();
//        List<Grade> sortedInitGrades = new ArrayList<>(initGrades);
//        sortedInitGrades.sort(gradeByIdComparator);
//        receivedGrades.sort(gradeByIdComparator);
//        Assert.assertTrue("Initial populating was incorrect", initGrades.equals(receivedGrades));
    }

    @Test
    public void deleteTest(){
        List<Grade> grades = gradeDao.getAll();
        Grade deletedGrade = grades.get(2);
        grades.remove(deletedGrade);
        gradeDao.delete(deletedGrade);
        List<Grade> receivedGrades = gradeDao.getAll();
        Assert.assertEquals(grades.size(), receivedGrades.size());
        grades.sort(gradeByIdComparator);
        receivedGrades.sort(gradeByIdComparator);
        Assert.assertEquals(grades, receivedGrades);
    }

    @Test
    public void createTest(){
        List<Grade> grades = gradeDao.getAll();
        Grade createdGrade = new Grade(initSubjects.get(1), LocalDate.now(), 5);
        gradeDao.create(createdGrade);
        grades.add(createdGrade);
        List<Grade> receivedGrades = gradeDao.getAll();
        Assert.assertEquals(grades.size(), receivedGrades.size());
        Assert.assertEquals(grades, receivedGrades);
    }

    @Test
    public void updateTest(){
        List<Grade> grades = gradeDao.getAll();
        Grade updatedGrade = grades.get(3);
        updatedGrade.setMark(15);
        gradeDao.update(updatedGrade);
        List<Grade> receivedGrades = gradeDao.getAll();
        grades.sort(gradeByIdComparator);
        receivedGrades.sort(gradeByIdComparator);
        Assert.assertEquals(grades, receivedGrades);
    }

    @Test
    public void getByIdTest(){
        List<Grade> grades = gradeDao.getAll();
        Grade expectedGrade = grades.get(2);
        Grade actualGrade = gradeDao.getById(expectedGrade.getId());
        Assert.assertEquals(expectedGrade, actualGrade);
    }

    @Test
    public void getByDateTest(){
        LocalDate checkedDate = LocalDate.of(2017, 5, 15);
        List<Grade> expectedGrades = new ArrayList<>();
        expectedGrades.add(initGrades.get(3));
        expectedGrades.add(initGrades.get(4));
        expectedGrades.add(initGrades.get(7));
        List<Grade> receivedGrades = gradeDao.getOnDate(checkedDate);
        Assert.assertEquals(receivedGrades.size(), expectedGrades.size());
        receivedGrades.sort(gradeByIdComparator);
        expectedGrades.sort(gradeByIdComparator);
        Assert.assertEquals(expectedGrades, receivedGrades);
    }

    @Test
    public void getBySubjectTest(){//todo ascending
        List<Grade> initGrades = gradeDao.getAll();
        Subject checkedSubject = initSubjects.get(1);
        List<Grade> expectedGrades = new ArrayList<>();
        initGrades.forEach((grade) -> {
            if(grade.getSubject().equals(checkedSubject)){
                expectedGrades.add(grade);
            }
        });
        List<Grade> receivedGrades = gradeDao.getOnSubject(checkedSubject, true);
        Assert.assertEquals(receivedGrades.size(), expectedGrades.size());
        receivedGrades.sort(gradeByIdComparator);
        expectedGrades.sort(gradeByIdComparator);
        Assert.assertEquals(expectedGrades, receivedGrades);
    }

    @Test
    //Method name starts from 'z' letter in order to be started the last
    public void z_deleteAllTest(){
        List<Grade> initGrades = gradeDao.getAll();
        if(initGrades.size() != 0){
            gradeDao.deleteAll();
            List<Grade> shouldBeEmptyGrades = gradeDao.getAll();
            Assert.assertTrue(shouldBeEmptyGrades.isEmpty());
        }else {
            Assert.assertTrue("Init collection was empty", false);
        }
    }
}
