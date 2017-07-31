package services;

import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;

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
//    private final static String DEFAULT_FILE_NAME = "grades.json";

    @Override
    public void addGrade(Grade addedGrade) throws AddingGradeException {

        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();

        validateDate(date);

        if(!isGraded(subject, date)){
            addedGrade.setId(getUId());
            grades.add(addedGrade);
        }else {
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }
    }

    @Override
    public List<Grade> fetchAllGrades() {
        return new ArrayList<>(grades);
    }

    @Override
    public List<Grade> fetchBySubject(long subject, boolean ascendingByDate) {
        List<Grade> gradesOnDate = retrieveGradesBySubject(subject);
        sortByDate(gradesOnDate, ascendingByDate);
        return gradesOnDate;
    }

    @Override
    public List<Grade> fetchByDate(LocalDate date) {
        List<Grade> gradesOnDate = retrieveGradesByDate(date);
        gradesOnDate.sort(Comparator.comparing(o -> o.getSubject().getTitle()));
        return gradesOnDate;
    }

    @Override
    public List<Subject> fetchAllSubjects() {
        HashSet<Subject> allSubjects = new HashSet<>();
        grades.forEach(currentGrade -> allSubjects.add(currentGrade.getSubject()));
        return new ArrayList<>(allSubjects);
    }

    @Override
    public Subject fetchSubject(long id) {
        for(Subject subject : fetchAllSubjects()){
            if(subject.getId().equals(id)){
                return subject;
            }
        }
        return null;
    }

    @Override
    public void deleteGrade(long id) {
        for(int i = 0; i < this.grades.size(); i++){
            if(grades.get(i).getId().equals(id)){
                grades.remove(i);
            }
        }
    }

    @Override
    public void deleteAll() {
        this.grades = new ArrayList<>();
    }

    @Override
    public double calculateAvgGrade(long subject) {
        final double[] averageGrade = {0};
        List<Grade> subjectGrades = retrieveGradesBySubject(subject);
        subjectGrades.forEach(grade -> averageGrade[0] += grade.getMark());
        return subjectGrades.isEmpty() ? 0 : averageGrade[0] / subjectGrades.size();
    }

    @Override
    public boolean isGraded(Subject subject, LocalDate date) {
        return !retrieveGradesByDate(date, retrieveGradesBySubject(subject.getId())).isEmpty();
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
}
