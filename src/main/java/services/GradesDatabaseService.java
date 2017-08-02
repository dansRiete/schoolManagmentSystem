package services;

import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import exceptions.DeletingSubjectException;
import exceptions.NoGradesException;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Aleks on 28.07.2017.
 */
public class GradesDatabaseService extends BaseGradesService {

    private GradeDao gradeDao;
    private SubjectDao subjectDao;
    private Logger logger = Logger.getLogger(GradesDatabaseService.class);

    public GradesDatabaseService(SqlSessionFactory sqlSessionFactory) {
        this.gradeDao = new GradeDao(sqlSessionFactory);
        this.subjectDao = new SubjectDao(sqlSessionFactory);
    }

    @Override
    public void addGrade(Grade addedGrade) throws AddingGradeException {
        logger.info("addGrade(Grade addedGrade) was called, addedGrade = " + addedGrade);
        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();
        int mark = addedGrade.getMark();
        validateDate(date);

        if(subject == null){
            throw new AddingGradeException("Subject can not be null");
        }
        if(isGraded(subject, date)){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }
        if(mark < 0){
            throw new AddingGradeException(NEGATIVE_MARK_MSG);
        }
        gradeDao.create(addedGrade);
        logger.info("addGrade(Grade addedGrade) , grade successfully created");
    }


    public void addSubject(String title) throws AddingSubjectException {
        logger.info("addSubject(String title) , title = " + title);
        Subject createdSubject = Subject.compose(title);
        if(isSubjectExists(title)){
            throw new AddingSubjectException("There can not be two subjects with the same title");
        }
        subjectDao.create(createdSubject);
        logger.info("addSubject(String title) , subject successfully created");
    }

    @Override
    public List<Grade> fetchAllGrades() {
        logger.info("fetchAllGrades() was called");
        List<Grade> fetchedGrades = gradeDao.getAll();
        logger.info("fetchAllGrades() fetched " + fetchedGrades.size() + " grades");
        return fetchedGrades;
    }

    @Override
    public List<Grade> fetchBySubject(long subjectId, boolean ascendingByDate) {
        logger.info("fetchBySubject(long subjectId, boolean ascendingByDate) was called, subjectId = " + subjectId + ", ascendingByDate = " + ascendingByDate);
        List<Grade> fetchedSubjects = gradeDao.getOnSubject(subjectId, ascendingByDate);
        logger.info("fetchBySubject(long subjectId, boolean ascendingByDate) Fetched " + fetchedSubjects.size() + " grades");
        return fetchedSubjects;
    }

    @Override
    public List<Grade> fetchByDate(LocalDate date) {
        logger.info("fetchByDate(LocalDate date) was called, date = " + date);
        List<Grade> fetchedGrades = gradeDao.getOnDate(date);
        logger.info("fetchByDate(LocalDate date) fetched " + fetchedGrades.size() + " grades");
        return gradeDao.getOnDate(date);
    }

    @Override
    public List<Grade> fetchBySubjectAndDate(long subjectId, LocalDate date) {
        logger.info("fetchByDate(LocalDate date) was called, date = " + date + ", subjectId = " + subjectId);
        return gradeDao.getOnDateAndSubject(subjectId, date);
    }

    @Override
    public List<Subject> fetchAllSubjects() {
        logger.info("fetchAllSubjects() was called");
        List<Subject> fetchedSubjects = subjectDao.getAll();
        logger.info("fetchAllSubjects() fetched " + fetchedSubjects.size());
        return subjectDao.getAll();
    }

    @Override
    public Subject fetchSubject(long id) {
        logger.info("fetchSubject(long id) was called, id = " + id);
        Subject fetchedSubject = subjectDao.getById(id);
        logger.info("fetchSubject(long id), fetched subject = " + fetchedSubject);
        return fetchedSubject;
    }

    @Override
    public void deleteGrade(long id) {
        logger.info("deleteGrade(long id) was called, id = " + id);
        gradeDao.delete(id);
        logger.info("deleteGrade(long id) was deleted)");
    }

    @Override
    public void deleteSubject(long subjectId) throws DeletingSubjectException{
        logger.info("deleteSubject(long subjectId) was called, id = " + subjectId);
        if(!fetchBySubject(subjectId, true).isEmpty()){
            throw new DeletingSubjectException("There is one or more grades in this subject");
        }
        subjectDao.delete(subjectId);
        logger.info("deleteSubject(long subjectId) completed");
    }

    @Override
    public void forceDeleteSubject(long subjectId) {
        logger.info("forceDeleteSubject(long subjectId) was called, id = " + subjectId);
        List<Long> gradesIds = fetchBySubject(subjectId, true).stream().map(Grade::getId).collect(Collectors.toList());
        gradeDao.delete(gradesIds);
        subjectDao.delete(subjectId);
        logger.info("forceDeleteSubject(long subjectId) completed");

    }

    @Override
    public void deleteAllGrades() {
        logger.info("deleteAllGrades() was called");
        gradeDao.deleteAll();
        logger.info("All grades were deleted");
    }

    @Override
    public double calculateAvgGrade(long subjectId, LocalDate selectedDate) throws NoGradesException{
        logger.info("calculateAvgGrade(long subjectId, LocalDate selectedDate) was called, subjectId = " +
                subjectId + ", selectedDate = " + selectedDate);
        double averageGrade;
        if(subjectId == 0 && selectedDate == null){
            if(!gradeDao.areAnyGrades()){
                throw new NoGradesException("There are no grades in database");
            }
            averageGrade = gradeDao.averageOfAll();
        }else if(subjectId != 0 && selectedDate == null){
            if(!gradeDao.areAnyGradesOnSubject(subjectId)){
                throw new NoGradesException("There are no grades on selected subject in database");
            }
            averageGrade = gradeDao.averageGradeBySubject(subjectId);
        }else if(subjectId == 0 && selectedDate != null){
            if(!gradeDao.areAnyGradesOnDate(selectedDate)){
                throw new NoGradesException("There are no grades on selected subject in database");
            }
            averageGrade = gradeDao.averageGradeByDate(selectedDate);
        }else {
            if(!gradeDao.areAnyGradesOnDateAndSubject(subjectId, selectedDate)){
                throw new NoGradesException("There are no grades on selected subject in database");
            }
            averageGrade = gradeDao.averageGradeBySubjectAndDate(subjectId, selectedDate);
        }
        logger.info("calculateAvgGrade(long subjectId, LocalDate selectedDate); completed");
        return averageGrade;
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        logger.info("isGraded(Subject subject, LocalDate date) was called, subjectId = " + subject.getId() +
                ", date = " + date);
        return gradeDao.isGraded(subject, date);
    }

    @Override
    public boolean isSubjectExists(String subjectTitle) {
        logger.info("isSubjectExists(String subjectTitle) was called");
        return subjectDao.isExists(subjectTitle);
    }

    @Override
    public void reloadFromFile(String fileName) throws IOException {

    }

    @Override
    public void dumpToFile(String fileName) throws IOException {

    }

}
