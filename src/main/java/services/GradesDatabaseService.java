package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Aleks on 28.07.2017.
 */
public class GradesDatabaseService extends BaseGradesService {

    private final int itemsPerPage = 5;
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

    public void addAllSubjects(List<Subject> subjects){
        subjectDao.createAll(subjects);
    }

    @Override
    public void addGrades(List<Grade> addedGrades) throws AddingGradeException {
        List<Subject> subjectsToAdd = extractSubjects(addedGrades);
        List<Subject> existedSubject = fetchAllSubjects();
        subjectsToAdd.removeAll(existedSubject);

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

    public List<Grade> fetchAllGrades() {
        logger.info("fetchAllGrades() was called");
        List<Grade> fetchedGrades = gradeDao.getAll();
        logger.info("fetchAllGrades() fetched " + fetchedGrades.size() + " grades");
        return fetchedGrades;
    }

    public List<Grade> fetchAllGrades(int page) {
        logger.info("fetchAllGrades(int page) was called, page = " + page);
        List<Grade> fetchedGrades = gradeDao.getAll(itemsPerPage, page * itemsPerPage);
        logger.info("fetchAllGrades() fetched " + fetchedGrades.size() + " grades");
        return fetchedGrades;
    }

    public int availablePagesNumber(long subjectId, LocalDate date) {
        logger.info("availablePagesNumber() was called");

        if(subjectId != 0 && date == null){
            return (int) Math.ceil(((double) gradeDao.countBySubject(subjectId)) / ((double) itemsPerPage));
        }else if(subjectId == 0 && date != null){
            return (int) Math.ceil(((double) gradeDao.countByDate(date)) / ((double) itemsPerPage));
        }else if(subjectId != 0 && date != null){
            return (int) Math.ceil(((double) gradeDao.countBySubjectAndDate(subjectId, date)) / ((double) itemsPerPage));
        }else {
            return (int) Math.ceil(((double) gradeDao.countAll()) / ((double) itemsPerPage));
        }

    }


    @Override
    public List<Grade> fetchGrades(long subjectId, LocalDate date) {
        logger.info("fetchByDate(LocalDate date) was called, date = " + date + ", subjectId = " + subjectId);

        if(subjectId != 0 && date == null){
            return gradeDao.getOnSubject(subjectId, true);
        }else if(subjectId == 0 && date != null){
            return gradeDao.getOnDate(date);
        }else if(subjectId != 0 && date != null){
            return gradeDao.getOnDateAndSubject(subjectId, date);
        }else {
            return gradeDao.getAll();
        }
    }

    public List<Grade> fetchGrades(long subjectId, LocalDate date, int page) {

        if(subjectId != 0 && date == null){
            return gradeDao.getOnSubject(subjectId, true, itemsPerPage, page * itemsPerPage);
        }else if(subjectId == 0 && date != null){
            return gradeDao.getOnDate(date, itemsPerPage, page * itemsPerPage);
        }else if(subjectId != 0 && date != null){
            return gradeDao.getOnDateAndSubject(subjectId, date, itemsPerPage, page * itemsPerPage);
        }else {
            return gradeDao.getAll(itemsPerPage, page * itemsPerPage);
        }
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
        if(!fetchGrades(subjectId, null).isEmpty()){
            throw new DeletingSubjectException("There is one or more grades in this subject");
        }
        subjectDao.delete(subjectId);
        logger.info("deleteSubject(long subjectId) completed");
    }

    @Override
    public void forceDeleteSubject(long subjectId) {
        logger.info("forceDeleteSubject(long subjectId) was called, id = " + subjectId);
        if(!fetchGrades(subjectId, null).isEmpty()){
            List<Long> gradesIds = fetchGrades(subjectId, null)
                    .stream().map(Grade::getId).collect(Collectors.toList());
            gradeDao.delete(gradesIds);
        }
        subjectDao.delete(subjectId);
        logger.info("forceDeleteSubject(long subjectId) completed");

    }

    @Override
    public void forceDeleteAllSubjects() {
        deleteAllGrades();
        subjectDao.deleteAll();
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
                throw new NoGradesException("There are no grades");
            }
            averageGrade = gradeDao.averageOfAll(); //todo return
        }else if(subjectId != 0 && selectedDate == null){
            if(!gradeDao.areAnyGradesOnSubject(subjectId)){
                throw new NoGradesException("There are no grades on selected subject");
            }
            averageGrade = gradeDao.averageGradeBySubject(subjectId);
        }else if(subjectId == 0 && selectedDate != null){
            if(!gradeDao.areAnyGradesOnDate(selectedDate)){
                throw new NoGradesException("There are no grades on selected date");
            }
            averageGrade = gradeDao.averageGradeByDate(selectedDate);
        }else {
            if(!gradeDao.areAnyGradesOnDateAndSubject(subjectId, selectedDate)){
                throw new NoGradesException("There are no grades on selected subject and date");
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
    public void fromJson(String json) throws IOException {
        Gson gson = new Gson();
        forceDeleteAllSubjects();
        List<Grade> jsonGrades = gson.fromJson(json, GRADES_LIST_REVIEW_TYPE);
        subjectDao.createAll(extractSubjects(jsonGrades));
        List<Subject> finalReadSubjects = subjectDao.getAll();
        jsonGrades.forEach(grade -> {
            final Subject[] currentSubject = {null};
            finalReadSubjects.forEach(subject -> {
                if(subject.getTitle().equals(grade.getSubject().getTitle())){
                    currentSubject[0] = subject;
                }
            });
            grade.setSubject(currentSubject[0]);
        });
        gradeDao.createAll(jsonGrades);
    }

    @Override
    public String toJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(fetchAllGrades());
    }

    @Override
    public void toJson(List<Grade> grades) throws IOException {

    }

}
