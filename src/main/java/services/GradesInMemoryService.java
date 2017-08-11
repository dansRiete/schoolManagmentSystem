package services;

import exceptions.*;
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

    private final int itemsPerPage = 5;
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

        addedGrade.setId(getGradeUId());
        grades.add(addedGrade);
        logger.info("addGrade(Grade addedGrade) , grade successfully created");
    }

    @Override
    public void addGrades(List<Grade> addedGrades) throws AddingGradeException {
        logger.info("addGrades(List<Grade> addedGrades) was called");
        grades.addAll(addedGrades);
    }

    @Override
    public void addSubject(String title) throws SubjectIllegalTitleException, SubjectExistsException {
        logger.info("addSubject(String title) , title = " + title);
        Subject addedSubject = Subject.compose(title);
        if(fetchAllSubjects().contains(addedSubject)){
            throw new SubjectExistsException();
        }
        addedSubject.setId(getSubjectUId());
        this.subjects.add(addedSubject);
        logger.info("addSubject(String title) , subject successfully created");
    }

    @Override
    public void addAllSubjects(List<Subject> subjects) {
        logger.info("addAllSubjects(List<Subject> subjects) was called");
        subjects.addAll(subjects);
    }

    @Override
    public List<Grade> fetchAllGrades() {
        return fetchGrades(0, null);
    }

    @Override
    public List<Grade> fetchGrades(long subjectId, LocalDate date) {
        return filterGrades(subjectId, date);
    }

    @Override
    public List<Grade> fetchGrades(long subjectId, LocalDate date, int page) {
        return filterGrades(subjectId, date).subList(page * itemsPerPage, page * itemsPerPage + (itemsPerPage > grades.size() ? grades.size() : itemsPerPage));
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
    public void deleteSubject(long subjectId) throws DeletingSubjectException {
        subjects.forEach(subject -> {
            if(subject.getId().equals(subjectId)){
                subjects.remove(subject);
            }
        });
    }

    @Override
    public void forceDeleteSubject(long subjectId) {
        grades.forEach(grade -> {
            if(grade.getSubject().getId().equals(subjectId)){
                grades.remove(grade);
            }
        });
        subjects.forEach(subject1 -> {
            if(subject1.getId().equals(subjectId)){
                subjects.remove(subject1);
            }
        });
    }

    @Override
    public void forceDeleteAllSubjects() {
        subjects = new ArrayList<>();
        grades = new ArrayList<>();
    }

    @Override
    public void deleteAllGrades() {
        logger.info("deleteAllGrades() was called");
        this.grades = new ArrayList<>();
        logger.info("All grades were deleted");
    }

    @Override
    public double calculateAvgGrade(long subjectId, LocalDate selectedDate) throws NoGradesException{
        logger.info("calculateAvgGrade(long subjectId, LocalDate selectedDate) was called, subjectId = " +
                subjectId + ", selectedDate = " + selectedDate);

        List<Grade> sortedGrade = filterGrades(subjectId, selectedDate);

        if(sortedGrade.isEmpty()){
            throw new NoGradesException("There are no grades on selected subject and date");
        }

        final int[] summ = {0};
        sortedGrade.forEach(grade -> summ[0] +=grade.getMark());

        double averageGrade = ((double) summ[0]) / ((double) sortedGrade.size());
        logger.info("calculateAvgGrade(long subjectId, LocalDate selectedDate); completed");
        return averageGrade;
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        logger.info("isGraded(Subject subject, LocalDate date) was called, subjectId = " +
                subject.getId() + ", date = " + date);
        return !retrieveGradesByDate(date, retrieveGradesBySubject(subject.getId())).isEmpty();
    }

    @Override
    public boolean isSubjectExists(String subjectTitle) {
        logger.info("isSubjectExists(String subjectTitle) was called, title = " + subjectTitle);
        Subject foundSubject = null;
        try {
            foundSubject = Subject.compose(subjectTitle);
        } catch (SubjectIllegalTitleException e) {
            logger.error("isSubjectExists(String subjectTitle) was called with illegal subjectTitle");
        }
        logger.info("isSubjectExists(String subjectTitle) found subject");
        return fetchAllSubjects().contains(foundSubject);
    }

    @Override
    public void reloadCollectionFromJson(String json) throws IOException {
        setGrades(fromJson(json));
    }

    @Override
    public int availablePagesNumber(long requestedSubjectId, LocalDate requestedDate) {
        return grades.size() / itemsPerPage;
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

    private long getGradeUId(){
        long id = 0;
        for(Grade grade : grades){
            if(grade.getId() > id){
                id = grade.getId();
            }
        }
        return id == 0 ? 1 : id;
    }

    private long getSubjectUId(){
        long id = 0;
        for(Subject subject : subjects){
            if(subject.getId() > id){
                id = subject.getId();
            }
        }
        return id == 0 ? 1 : id;
    }

    private List<Grade> filterGrades(long subjectId, LocalDate selectedDate){
        List<Grade> sortedGrade;

        if(subjectId == 0 && selectedDate == null){
            sortedGrade = grades;
        }else if(subjectId != 0 && selectedDate == null){
            sortedGrade = grades.stream().filter(
                    grade -> grade.getSubject().getId() == subjectId).collect(Collectors.toList()
            );
        }else if(subjectId == 0 && selectedDate != null){
            sortedGrade = grades.stream().filter(
                    grade -> grade.getDate().equals(selectedDate)).collect(Collectors.toList()
            );
        }else {
            sortedGrade = grades.stream().filter(
                    grade -> grade.getDate().equals(selectedDate) && grade.getSubject().getId() == subjectId
            ).collect(Collectors.toList());
        }

        return sortedGrade;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
