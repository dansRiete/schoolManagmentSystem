package services;

import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
public class GradesDatabaseService extends BaseGradesService {

    private GradeDao gradeDao;
    private SubjectDao subjectDao;

    public GradesDatabaseService(SqlSessionFactory sqlSessionFactory) {
        this.gradeDao = new GradeDao(sqlSessionFactory);
        this.subjectDao = new SubjectDao(sqlSessionFactory);
    }

    @Override
    public void addGrade(Grade addedGrade) throws AddingGradeException {
        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();
        int mark = addedGrade.getMark();
        validateDate(date);

        if(subject == null){
            throw new AddingGradeException("Subject can not be null");
        }else if(date == null){
            throw new AddingGradeException("Date can not be null");
        }else if(isGraded(subject, date)){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }else if(mark < 0){
            throw new AddingGradeException(NEGATIVE_MARK_MSG);
        }else {
            gradeDao.create(addedGrade);
        }
    }

    public void addSubject(Subject subject) {
        subjectDao.create(subject);
    }

    @Override
    public List<Grade> fetchAllGrades() {
        return gradeDao.getAll();
    }

    @Override
    public List<Grade> fetchBySubject(long subjectId, boolean ascendingByDate) {
        return gradeDao.getOnSubject(subjectId, ascendingByDate);
    }

    @Override
    public List<Grade> fetchByDate(LocalDate date) {
        return gradeDao.getOnDate(date);
    }

    @Override
    public List<Subject> fetchAllSubjects() {
        return subjectDao.getAll();
    }

    @Override
    public Subject fetchSubject(long id) {
        return subjectDao.getById(id);
    }

    @Override
    public void deleteGrade(long id) {
        gradeDao.delete(id);
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public double calculateAvgGrade(long subject) {
        return gradeDao.averageGrade(subject);
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        return gradeDao.isGraded(subject, date);
    }

    @Override
    public void reloadFromFile(String fileName) throws IOException {

    }

    @Override
    public void dumpToFile(String fileName) throws IOException {

    }

}
