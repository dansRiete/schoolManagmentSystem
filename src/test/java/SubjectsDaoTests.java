import dao.SubjectDao;
import exceptions.IllegalTitleException;
import model.Subject;
import model.Subject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import services.MyBatisTestService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Aleks on 27.07.2017.
 */
@SuppressWarnings("Duplicates")
public class SubjectsDaoTests {
    
    private static SubjectDao subjectDao = new SubjectDao(MyBatisTestService.getSqlSessionFactory());
    private static List<Subject> initSubjects;
    private final static Comparator<Subject> subjectByIdComparator = Comparator.comparing(Subject::getId);
//    final static Logger logger = Logger.getLogger(SubjectsDaoTests.class);

    @BeforeClass
    public static void populate() throws IllegalTitleException {
        subjectDao.deleteAll();
        initSubjects = Arrays.asList(
                Subject.compose("Math"),
                Subject.compose("Geographic"),
                Subject.compose("Biology"),
                Subject.compose("Literature"),
                Subject.compose("History")
        );
        subjectDao.create(initSubjects);
        initSubjects = subjectDao.getAll();
        List<Subject> sortedInitSubjects = new ArrayList<>(initSubjects);
        sortedInitSubjects.sort(subjectByIdComparator);
        List<Subject> receivedSubjects = subjectDao.getAll();
        receivedSubjects.sort(subjectByIdComparator);
        Assert.assertTrue("Initial populating was incorrect", initSubjects.equals(receivedSubjects));
    }

    @Test
    //Method name starts from 'a' letter in order to be started the first
    public void a_deleteTest(){
        List<Subject> subjects = subjectDao.getAll();
        Subject deletedSubject = subjects.get(0);
        subjects.remove(deletedSubject);
        subjectDao.delete(deletedSubject);
        List<Subject> receivedSubjects = subjectDao.getAll();
        Assert.assertEquals(subjects.size(), receivedSubjects.size());
        subjects.sort(subjectByIdComparator);
        receivedSubjects.sort(subjectByIdComparator);
        Assert.assertEquals(subjects, receivedSubjects);
    }

    @Test
    public void createTest() throws IllegalTitleException {
        List<Subject> subjects = subjectDao.getAll();
        Subject createdSubject = Subject.compose("SomeSubject");
        subjectDao.create(createdSubject);
        subjects.add(createdSubject);
        List<Subject> receivedSubjects = subjectDao.getAll();
        Assert.assertEquals(subjects.size(), receivedSubjects.size());
        Assert.assertEquals(subjects, receivedSubjects);
    }

    @Test
    public void updateTest(){
        List<Subject> subjects = subjectDao.getAll();
        Subject updatedSubject = subjects.get(1);
        updatedSubject.setTitle("NewTitle");
        subjectDao.update(updatedSubject);
        List<Subject> receivedSubjects = subjectDao.getAll();
        subjects.sort(subjectByIdComparator);
        receivedSubjects.sort(subjectByIdComparator);
        Assert.assertEquals(subjects, receivedSubjects);
    }

    @Test
    public void getByIdTest(){
        List<Subject> subjects = subjectDao.getAll();
        Subject expectedSubject = subjects.get(2);
        Subject actualSubject = subjectDao.getById(expectedSubject.getId());
        Assert.assertEquals(expectedSubject, actualSubject);
    }

    @Test
    //Method name starts from 'z' letter in order to be started the last
    public void z_deleteAllTest(){
        List<Subject> initSubjects = subjectDao.getAll();
        if(initSubjects.size() != 0){
            subjectDao.deleteAll();
            List<Subject> shouldBeEmptySubjects = subjectDao.getAll();
            Assert.assertTrue(shouldBeEmptySubjects.isEmpty());
        }else {
            Assert.assertTrue("Init collection was empty", false);
        }
    }
}
