import datasources.DataSourceTest;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import mappers.SqlMapper;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import services.GradesDatabaseService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aleks on 31.07.2017.
 */
@SuppressWarnings("Duplicates")
public class DatabaseServiceTests {

    SqlSessionFactory sqlSessionFactory = DataSourceTest.getSqlSessionFactory();
    GradesDatabaseService gradesService = new GradesDatabaseService(DataSourceTest.getSqlSessionFactory());
    Subject math_id1;
    Subject geo_id2;
    Subject history_id3;
    Grade grade1;
    Grade grade2;
    Grade grade3;
    Grade grade4;
    Grade grade5;
    Grade grade6;
    List<Subject> allInitSubjects = new ArrayList<>();
    List<Grade> allInitGrades = new ArrayList<>();
    List<Grade> initMathGradesAscendingDate = new ArrayList<>();
    List<Grade> initMathGradesDescendingDate = new ArrayList<>();
    {
        try {
            math_id1 = Subject.compose("Math");
            math_id1.setId(1L);
            geo_id2 = Subject.compose("Geo");
            geo_id2.setId(2L);
            history_id3 = Subject.compose("History");
            history_id3.setId(3L);
            grade1 = new Grade(history_id3, LocalDate.of(2017, 7, 30), 3);
            grade2 = new Grade(geo_id2, LocalDate.of(2017, 7, 29), 10);
            grade3 = new Grade(history_id3, LocalDate.of(2017, 7, 29), 8);
            grade4 = new Grade(math_id1, LocalDate.of(2017, 7, 20), 6);
            grade5 = new Grade(math_id1, LocalDate.of(2017, 7, 18), 4);
            grade6 = new Grade(math_id1, LocalDate.of(2017, 7, 17), 3);
            grade1.setId(1L);
            grade2.setId(2L);
            grade3.setId(3L);
            grade4.setId(4L);
            grade5.setId(5L);
            grade6.setId(6L);
            initMathGradesAscendingDate.add(grade6);
            initMathGradesAscendingDate.add(grade5);
            initMathGradesAscendingDate.add(grade4);
            initMathGradesDescendingDate.add(grade4);
            initMathGradesDescendingDate.add(grade5);
            initMathGradesDescendingDate.add(grade6);
            allInitSubjects.add(math_id1);
            allInitSubjects.add(history_id3);
            allInitSubjects.add(geo_id2);
            allInitGrades.add(grade1);
            allInitGrades.add(grade2);
            allInitGrades.add(grade3);
            allInitGrades.add(grade4);
            allInitGrades.add(grade5);
            allInitGrades.add(grade6);
        } catch (AddingSubjectException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void clear() throws AddingSubjectException {
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
                ";\n" +
                "\n" +
                "create unique index subjects_id_uindex\n" +
                "\ton subjects (id)\n" +
                ";\n" +
                "\n" +
                "create unique index subjects_title_uindex\n" +
                "\ton subjects (title)\n" +
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
        String sql8 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-30', 3, 3)";
        String sql9 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-29', 2, 10)";
        String sql10 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-29', 3, 8)";
        String sql11 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-20', 1, 6)";
        String sql12 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-18', 1, 4)";
        String sql13 = "INSERT INTO grades (date, subject_id, mark) VALUES ('2017-07-17', 1, 3)";
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
    public void populateSubjectsTest(){
        List<Subject> receivedSubjects = gradesService.fetchAllSubjects();
        assertThat(receivedSubjects, containsInAnyOrder(math_id1, geo_id2, history_id3));
    }

    @Test
    public void populateGradesTest(){
        List<Grade> receivedGrades = gradesService.fetchAllGrades();
        assertThat(receivedGrades, containsInAnyOrder(grade1, grade2, grade3, grade4, grade5, grade6));
    }

    @Test
    public void getSubjectByIdTest(){
        assertEquals(math_id1, gradesService.fetchSubject(1L));
        assertEquals(history_id3, gradesService.fetchSubject(3L));
        assertEquals(geo_id2, gradesService.fetchSubject(2L));
        assertNotEquals(geo_id2, gradesService.fetchSubject(3L));
    }

    @Test
    public void createGradeTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, LocalDate.of(2017, 6, 14), 8);
        Grade absentGrade = new Grade(math_id1, LocalDate.of(2017, 4, 14), 8);
        gradesService.addGrade(addedGrade);
        assertTrue(gradesService.fetchAllGrades().contains(addedGrade));
        assertFalse(gradesService.fetchAllGrades().contains(absentGrade));

    }



    @Test
    public void createAllSubjectsTest() throws AddingGradeException, AddingSubjectException {
        Subject subject1 = Subject.compose("First");
        Subject subject2 = Subject.compose("Second");
        Subject subject3 = Subject.compose("Third");
//        Grade absentGrade = new Grade(math_id1, LocalDate.of(2017, 4, 14), 8);
        gradesService.addAllSubjects(Arrays.asList(subject1, subject2, subject3));
        assertThat(gradesService.fetchAllSubjects(), containsInAnyOrder(subject1, subject2, subject3));

    }

    @Test(expected = AddingGradeException.class)
    public void createGradeOnTheSameDateTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, LocalDate.of(2017, 7, 20), 3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingGradeException.class)
    public void createWithNullDateTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, null, 3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingGradeException.class)
    public void createWithNegativeMarkTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, LocalDate.of(2017, 6, 20), -3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingGradeException.class)
    public void createGradeWithNullSubjectTest() throws AddingGradeException {
        Grade addedGrade = new Grade(null, LocalDate.now(), 3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingGradeException.class)
    public void createWithPastYearDateTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, LocalDate.now().minusYears(1), 3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingGradeException.class)
    public void createWithTomorowDateTest() throws AddingGradeException {
        Grade addedGrade = new Grade(math_id1, LocalDate.now().plusMonths(1), 3);
        gradesService.addGrade(addedGrade);

    }

    @Test(expected = AddingSubjectException.class)
    public void createSubjectWithAlreadyExistedTitleTest() throws AddingGradeException, AddingSubjectException {
        gradesService.addSubject("Math");
    }

    @Test
    public void calculateAverageGradeTest(){
//        assertEquals(4.3333, gradesService.calculateAvgGrade(math_id1.getId()), 0.001);//todo
    }

    @Test
    public void addSubjectTest() throws AddingSubjectException {
        Subject addedSubject = Subject.compose("Biology");
        gradesService.addSubject(addedSubject.getTitle());
        assertTrue(gradesService.fetchAllSubjects().contains(addedSubject));
    }

    @Test
    public void deleteAllGradesTest() {
        gradesService.deleteAllGrades();
        assertThat(gradesService.fetchAllGrades().size(), is(0));
    }

    @Test
    public void fetchBySubjectAscendingDateTest(){
//        assertEquals(initMathGradesAscendingDate, gradesService.fetchBySubject(1, true));
    }

    @Test
    public void fetchBySubjectDescendingDateTest(){
//        assertEquals(initMathGradesDescendingDate, gradesService.fetchBySubject(1, false));
    }

    @Test
    public void deleteGradeTest(){
        gradesService.deleteGrade(3);
        assertFalse(gradesService.fetchAllGrades().contains(grade3));
    }

    @Test
    public void fetchByDateTestHamcrest(){
//        List<Grade> receivedList = gradesService.fetchByDate(LocalDate.of(2017, 7, 29));
//        assertThat(receivedList, containsInAnyOrder(grade2, grade3));
    }

}
