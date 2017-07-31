import dao.GradeDao;
import dao.SubjectDao;
import datasources.DataSourceH2Test;
import datasources.DataSourceTest;
import exceptions.AddingGradeException;
import exceptions.IllegalSubjectTitleException;
import mappers.GradeMapper;
import mappers.SqlMapper;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.BaseGradesService;
import services.GradesDatabaseService;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Aleks on 31.07.2017.
 */
public class DatabaseH2inMemoryTests {

    SqlSessionFactory sqlSessionFactory = DataSourceTest.getSqlSessionFactory();
    SubjectDao subjectDao = new SubjectDao(sqlSessionFactory);
    GradeDao gradeDao = new GradeDao(sqlSessionFactory);
    GradesDatabaseService gradesService = new GradesDatabaseService(sqlSessionFactory);
    Subject math;
    Subject geo;
    Subject history;
    Grade grade1;
    Grade grade2;
    Grade grade3;
    Grade grade4;
    Grade grade5;
    Grade grade6;
    List<Subject> allSubjects = new ArrayList<>();
    List<Grade> allGrades = new ArrayList<>();
    private final static Comparator<Grade> gradeByIdComparator = Comparator.comparing(Grade::getId);
    private final static Comparator<Subject> subjectByIdComparator = Comparator.comparing(Subject::getId);
    {
        try {
            math = Subject.compose("Math");
            math.setId(1L);
            geo = Subject.compose("Geo");
            geo.setId(2L);
            history = Subject.compose("History");
            history.setId(3L);
            grade1 = new Grade(history, LocalDate.of(2017, 7, 30), 3);
            grade2 = new Grade(geo, LocalDate.of(2017, 7, 29), 10);
            grade3 = new Grade(history, LocalDate.of(2017, 7, 29), 8);
            grade4 = new Grade(math, LocalDate.of(2017, 7, 20), 6);
            grade5 = new Grade(math, LocalDate.of(2017, 7, 18), 4);
            grade6 = new Grade(math, LocalDate.of(2017, 7, 17), 3);
            grade1.setId(1L);
            grade2.setId(2L);
            grade3.setId(3L);
            grade4.setId(4L);
            grade5.setId(5L);
            grade6.setId(6L);
            allSubjects.add(math);
            allSubjects.add(history);
            allSubjects.add(geo);
            allGrades.add(grade1);
            allGrades.add(grade2);
            allGrades.add(grade3);
            allGrades.add(grade4);
            allGrades.add(grade5);
            allGrades.add(grade6);
            allSubjects.sort(subjectByIdComparator);
            allGrades.sort(gradeByIdComparator);
        } catch (IllegalSubjectTitleException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void clear() throws IllegalSubjectTitleException {
        SqlSession sqlSession = null;
        String sql1 = "DROP TABLE IF EXISTS grades";
        String sql2 = "DROP TABLE IF EXISTS subjects";
        String sql3 = "create table subjects\n" +
                "(\n" +
                "\tid bigserial not null\n" +
                "\t\tconstraint subjects_pkey\n" +
                "\t\t\tprimary key,\n" +
                "\ttitle varchar(32) not null\n" +
                ")\n" +
                ";";
        String sql4 = "create table grades\n" +
                "(\n" +
                "\tid bigserial not null\n" +
                "\t\tconstraint grades_pkey\n" +
                "\t\t\tprimary key,\n" +
                "\tdate timestamp not null,\n" +
                "\tsubject_id bigint not null\n" +
                "\t\tconstraint grades_to_subjects_fk\n" +
                "\t\t\treferences subjects,\n" +
                "\tmark integer not null\n" +
                ")\n" +
                ";";
        String sql5 = "INSERT INTO subjects (title) VALUES ('Math')";
        String sql6 = "INSERT INTO subjects (title) VALUES ('Geo')";
        String sql7 = "INSERT INTO subjects (title) VALUES ('History')";
        String sql8 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-30', 3, 7)";
        String sql9 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-29', 2, 10)";
        String sql10 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-29', 3, 8)";
        String sql11 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-20', 1, 6)";
        String sql12 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-18', 1, 4)";
        String sql13 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-17', 1, 3)";
//        String sql13 = "SELECT avg(grades.mark) FROM grades WHERE subject_id = #{id}";
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(SqlMapper.class).select(sql1);
            sqlSession.getMapper(SqlMapper.class).select(sql2);
            sqlSession.getMapper(SqlMapper.class).select(sql3);
            sqlSession.getMapper(SqlMapper.class).select(sql4);
            sqlSession.getMapper(SqlMapper.class).select(sql5);
            sqlSession.getMapper(SqlMapper.class).select(sql6);
            sqlSession.getMapper(SqlMapper.class).select(sql7);
            sqlSession.getMapper(SqlMapper.class).select(sql8);
            sqlSession.getMapper(SqlMapper.class).select(sql9);
            sqlSession.getMapper(SqlMapper.class).select(sql10);
            sqlSession.getMapper(SqlMapper.class).select(sql11);
            sqlSession.getMapper(SqlMapper.class).select(sql12);
            sqlSession.getMapper(SqlMapper.class).select(sql13);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Test
    public void populateTest(){
        List<Subject> receivedSubjects = gradesService.fetchAllSubjects();
        receivedSubjects.sort(subjectByIdComparator);
        Assert.assertEquals(allSubjects, receivedSubjects);
    }

    @Test
    public void getSubjectByIdTest(){
        Assert.assertEquals(math, gradesService.fetchSubject(1L));
        Assert.assertEquals(history, gradesService.fetchSubject(3L));
        Assert.assertEquals(geo, gradesService.fetchSubject(2L));
        Assert.assertNotEquals(geo, gradesService.fetchSubject(3L));
    }

    @Test
    public void createGradeTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math, LocalDate.of(2017, 6, 14), 8);
        Grade absentGrade = new Grade(math, LocalDate.of(2017, 4, 14), 8);
        gradesService.addGrade(addedGrade);
        Assert.assertTrue(gradesService.fetchAllGrades().contains(addedGrade));
        Assert.assertFalse(gradesService.fetchAllGrades().contains(absentGrade));

    }

    @Test
    public void calculateAverageGradeTest(){
        Assert.assertEquals(4.3333, gradesService.calculateAvgGrade(math.getId()), 0.001);
        Assert.assertNotEquals(gradesService.calculateAvgGrade(math.getId()), 5, 0.001);
        gradesService.
    }

}
