package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dao.GradeDao;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nromanen on 7/24/2017.
 */
public class GradesService {

    private List<Grade> grades = new ArrayList<>();
    private GradeDao gradeDao;
    private final static String TWO_GRADES_ON_DAY_MSG = "There can not be two grades on the same subject on the same day";
    private final static String AFTER_TODAY_GRADE_MSG = "Grade date can not be after today";
    private final static String PAST_YEAR_GRADE_MSG = "Grade date can not be before beginning of the year";

    public GradesService(SqlSessionFactory sqlSessionFactory) {
        gradeDao = new GradeDao(sqlSessionFactory);
    }

    public void addGrade(Grade grade) throws AddingGradeException {
        isValidDate(grade.getDate());
        isGraded(grade.getSubject(), this.grades);
        grades.add(grade);
    }

    private void isValidDate(LocalDate date) throws AddingGradeException {
        if(date.isBefore(LocalDate.now().withDayOfMonth(1).withDayOfYear(1))){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(date.isAfter(LocalDate.now())){
            throw new AddingGradeException(AFTER_TODAY_GRADE_MSG);
        }
    }

    public void addGradeToDb(Grade addedGrade) throws AddingGradeException {
        isValidDate(addedGrade.getDate());
        if(gradeDao.isGraded(addedGrade.getSubject(), addedGrade.getDate())){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }
        gradeDao.create(addedGrade);
    }

    public List<Grade> getGrades(){
        return new ArrayList<>(grades);
    }

    public List<Grade> getGradesFromDb(){
        return gradeDao.getAll();
    }

    public List<Grade> getGrades(LocalDate date){
        List<Grade> gradesOnDate = retrieveGradesByDate(date);
        gradesOnDate.sort(Comparator.comparing(o -> o.getSubject().getTitle()));
        return gradesOnDate;
    }

    public List<Grade> getGradesFromDb(LocalDate date){
        return gradeDao.getOnDate(date);
    }

    public List<Grade> getGrades(Subject subject, boolean ascendingDate){
        List<Grade> gradesOnDate = retrieveGradesBySubject(subject, this.grades);
        sortByDate(gradesOnDate, ascendingDate);
        return gradesOnDate;
    }

    public List<Grade> getGradesFromDb(Subject subject, boolean ascendingDate){
        List<Grade> gradesOnSubject = gradeDao.getOnSubject(subject, ascendingDate);
        sortByDate(gradesOnSubject, ascendingDate);
        return gradesOnSubject;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public double calculateAvgGrade(Subject subject){
        final double[] averageGrade = {0};
        List<Grade> subjectGrades = retrieveGradesBySubject(subject, this.grades);
        subjectGrades.forEach(grade -> averageGrade[0] += grade.getMark());
        return subjectGrades.isEmpty() ? 0 : averageGrade[0] / subjectGrades.size();
    }

    public Double calculateAvgGradeOnDb(Subject subject){
        return gradeDao.averageGrade(subject);
    }

    private void validate(LocalDate date, Subject subject, List<Grade> grades) throws AddingGradeException {
        if(date.isBefore(LocalDate.now().withDayOfMonth(1).withDayOfYear(1))){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(date.isAfter(LocalDate.now())){
            throw new AddingGradeException(AFTER_TODAY_GRADE_MSG);
        }
        if(isGraded(subject, grades)){
            throw new AddingGradeException(TWO_GRADES_ON_DAY_MSG);
        }
    }

    private boolean isGraded(Subject subject, List<Grade> grades) {
        return grades.isEmpty() || !retrieveGradesBySubject(subject, grades).isEmpty();
    }

    private List<Grade> retrieveGradesBySubject(Subject subject, List<Grade> grades){
        return grades.stream()
                .filter(currentGrade -> currentGrade.getSubject().equals(subject))
                .collect(Collectors.toList());
    }

    private List<Grade> retrieveGradesByDate(LocalDate date){
        return this.grades.stream()
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

}
