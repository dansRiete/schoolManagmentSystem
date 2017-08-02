package services;

import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import exceptions.NoGradesException;
import model.Grade;
import model.Subject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Aleks on 28.07.2017.
 */
public class GradesInMemoryService extends BaseGradesService {

    private List<Grade> grades = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private Logger logger = Logger.getLogger(GradesDatabaseService.class);

    @Override
    public void addGrade(Grade addedGrade) throws AddingGradeException {
        logger.info("addGrade(Grade addedGrade) was called, addedGrade = " + addedGrade);

        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();
        validateDate(date);

        if(addedGrade.getSubject() == null){
            throw new AddingGradeException("Subject cannot be null");
        }
        if(addedGrade.getMark() < 0){
            throw new AddingGradeException("Mark cannot be negative");
        }
        if(isGraded(subject, date)){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }

        addedGrade.setId(getUId());
        grades.add(addedGrade);
        logger.info("addGrade(Grade addedGrade) , grade successfully created");
    }

    @Override
    public void addSubject(String title) throws AddingSubjectException {
        logger.info("addSubject(String title) , title = " + title);
        Subject addedSubject = Subject.compose(title);
        if(fetchAllSubjects().contains(addedSubject)){
            throw new AddingSubjectException("There can not be two subjects with the same names");
        }
        this.subjects.add(addedSubject);
        logger.info("addSubject(String title) , subject successfully created");
    }

    @Override
    public List<Grade> fetchAllGrades() {
        logger.info("fetchAllGrades() was called, fetched " + grades.size() + " grades");
        return new ArrayList<>(grades);
    }

    @Override
    public List<Grade> fetchBySubject(long subjectId, boolean ascendingByDate) {
        logger.info("fetchBySubject(long subjectId, boolean ascendingByDate) was called, subjectId = " + subjectId + ", ascendingByDate = " + ascendingByDate);
        List<Grade> gradesOnDate = retrieveGradesBySubject(subjectId);
        logger.info("fetchBySubject(long subjectId, boolean ascendingByDate) Fetched " + gradesOnDate.size() + " grades");
        sortByDate(gradesOnDate, ascendingByDate);
        return gradesOnDate;
    }

    @Override
    public List<Grade> fetchByDate(LocalDate date) {
        logger.info("fetchByDate(LocalDate date) was called, date = " + date);
        List<Grade> gradesOnDate = retrieveGradesByDate(date);
        gradesOnDate.sort(Comparator.comparing(o -> o.getSubject().getTitle()));
        logger.info("fetchByDate(LocalDate date) fetched " + gradesOnDate.size() + " grsdes");
        return gradesOnDate;
    }

    @Override
    public List<Grade> fetchBySubjectAndDate(long subjectId, LocalDate date) {
        return null;//todo to implement
    }

    @Override
    public List<Subject> fetchAllSubjects() {
        logger.info("fetchAllSubjects() was called");
        HashSet<Subject> allSubjects = new HashSet<>();
        grades.forEach(currentGrade -> allSubjects.add(currentGrade.getSubject()));
        allSubjects.addAll(subjects);
        logger.info("fetchAllSubjects() fetched " + allSubjects.size());
        return new ArrayList<>(allSubjects);
    }

    @Override
    public Subject fetchSubject(long id) {
        logger.info("fetchSubject(long id) was called, id = " + id);
        for(Subject subject : fetchAllSubjects()){
            if(subject.getId().equals(id)){
                logger.info("fetchSubject(long id), fetched subject = " + subject);
                return subject;
            }
        }
        logger.info("fetchSubject(long id), fetched subject = null");
        return null;
    }

    @Override
    public void deleteGrade(long id) {
        logger.info("deleteGrade(long id) was called, id = " + id);
        for(int i = 0; i < this.grades.size(); i++){
            if(grades.get(i).getId().equals(id)){
                logger.info("deleteGrade(long id) was deleted)");
                grades.remove(i);
                return;
            }
        }
        logger.info("deleteGrade(long id) grade with id = " + id + "was not deleted, since was not found");
    }

    @Override
    public void deleteAllGrades() {
        logger.info("deleteAllGrades() was called");
        this.grades = new ArrayList<>();
        logger.info("All grades were deleted");
    }/*

    @Override
    public double calculateAvgGrade(long subjectId) {
        logger.info("calculateAvgGrade(long subjectId) was called, subjectId = " + subjectId);
        final double[] averageGrade = {0};
        List<Grade> subjectGrades = retrieveGradesBySubject(subjectId);
        subjectGrades.forEach(grade -> averageGrade[0] += grade.getMark());
        logger.info("calculateAvgGrade(long subjectId); completed");
        return subjectGrades.isEmpty() ? 0 : averageGrade[0] / subjectGrades.size();
    }*/

    @Override
    public double calculateAvgGrade(long subjectId, LocalDate selectedDate) throws NoGradesException{
        return 0;//todo to implement
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        logger.info("isGraded(Subject subject, LocalDate date) was called, subjectId = " + subject.getId() + ", date = " + date);
        return !retrieveGradesByDate(date, retrieveGradesBySubject(subject.getId())).isEmpty();
    }

    @Override
    public boolean isSubjectExists(String subjectTitle) {
        logger.info("isSubjectExists(String subjectTitle) was called, title = " + subjectTitle);
        Subject foundSubject = null;
        try {
            foundSubject = Subject.compose(subjectTitle);
        } catch (AddingSubjectException e) {
            logger.error("isSubjectExists(String subjectTitle) was called with illegal subjectTitle");
        }
        logger.info("isSubjectExists(String subjectTitle) found subject");
        return fetchAllSubjects().contains(foundSubject);
    }

    @Override
    public void reloadFromFile(String fileName) throws IOException {
        this.grades = readFromFile(fileName);
    }

    @Override
    public void dumpToFile(String fileName) throws IOException {
        saveToFile(fileName, this.grades);
    }

    private List<Grade> retrieveGradesBySubject(long subject){
        return grades.stream()
                .filter(currentGrade -> currentGrade.getSubject().getId().equals(subject))
                .collect(Collectors.toList());
    }

    private List<Grade> retrieveGradesByDate(LocalDate date){
        return grades.stream()
                .filter(currentGrade -> currentGrade.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private List<Grade> retrieveGradesByDate(LocalDate date, List<Grade> grades){
        return grades.stream()
                .filter(currentGrade -> currentGrade.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private void sortByDate(List<Grade> gradesOnDate, boolean ascendingDate){
        if(ascendingDate){
            gradesOnDate.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        }else {
            gradesOnDate.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        }
    }

    private long getUId(){
        long id = 0;
        for(Grade grade : grades){
            if(grade.getId() > id){
                id = grade.getId();
            }
        }
        return id == 0 ? 1 : id;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
