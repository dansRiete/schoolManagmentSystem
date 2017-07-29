package services;

import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSessionFactory;

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
        validateDate(date);

        if(subject == null){
            throw new AddingGradeException("Subject can not be null");
        }else if(date == null){
            throw new AddingGradeException("Date can not be null");
        }else if(isGraded(subject, date)){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }else {
            gradeDao.create(addedGrade);
        }
    }

    @Override
    public List<Grade> fetchAllGrades() {
        return gradeDao.getAll();
    }

    @Override
    public List<Grade> fetchBySubject(Subject subject, boolean ascendingByDate) {
        return gradeDao.getOnSubject(subject, ascendingByDate);
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
    public double calculateAvgGrade(Subject subject) {
        return gradeDao.averageGrade(subject);
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        return gradeDao.isGraded(subject, date);
    }

}
