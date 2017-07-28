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
    }

    @Override
    void addGrade(Grade addedGrade) throws AddingGradeException {
        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();

        validateDate(date);

        if(!isGraded(subject, date)){
            gradeDao.create(addedGrade);
        }else {
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }
    }

    @Override
    public List<Grade> fetchAllGrades() {
        return gradeDao.getAll();
    }

    @Override
    List<Grade> fetchBySubject(Subject subject, boolean ascendingByDate) {
        return gradeDao.getOnSubject(subject, ascendingByDate);
    }

    @Override
    List<Grade> fetchByDate(LocalDate date) {
        return gradeDao.getOnDate(date);
    }

    @Override
    List<Subject> fetchAllSubjects() {
        return subjectDao.getAll();
    }

    @Override
    double calculateAvgGrade(Subject subject) {
        return gradeDao.averageGrade(subject);
    }

    @Override
    boolean isGraded(Subject subject, LocalDate date) {
        return gradeDao.isGraded(subject, date);
    }

}
