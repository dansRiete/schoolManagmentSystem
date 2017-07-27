import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dao.GradeDao;
import dao.SubjectDao;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;
import services.MyBatisService;

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
    private GradeDao gradeDao = new GradeDao(MyBatisService.getSqlSessionFactory());
    private SubjectDao subjectDao = new SubjectDao(MyBatisService.getSqlSessionFactory());
    private static final Type REVIEW_TYPE = new TypeToken<List<Grade>>() {}.getType();

    public List<Grade> readFromFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (
                FileReader fileReader = new FileReader(fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ){
            return gson.fromJson(jsonReader, REVIEW_TYPE);
        } catch (IOException e) {
            throw e;
        }
    }

    public void writeToFile(String fileName, List<Grade> writtenGrades) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(writtenGrades, writer);
        }catch (IOException e){
            throw e;
        }
    }

    public void addGrade(Grade grade) throws AddingGradeException {
        validate(grade.getDate(), grade.getSubject());
        grades.add(grade);
    }

    private void validate(LocalDate date, Subject subject) throws AddingGradeException {
        if(date.isBefore(LocalDate.now().withDayOfMonth(1).withDayOfYear(1))){
            throw new AddingGradeException("Grade date can not be before beginning of the year");
        }
        if(date.isAfter(LocalDate.now())){
            throw new AddingGradeException("Grade date can not be after today");
        }
        if(isAlreadyGraded(subject, this.grades)){
            throw new AddingGradeException("There can not be two grades on the same subject on the same day");
        }
    }

    public void addGradeToDb(Grade grade) throws AddingGradeException {
        List<Grade> persistedGrades = gradeDao.getAll();
        validate(grade.getDate(), grade.getSubject(), persistedGrades);
        gradeDao.create(grade);
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
        List<Grade> gradesOnSubject = gradeDao.getOnSubject(subject);
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

    public double calculateAvgGradeOnDb(Subject subject){ //todo
        final double[] averageGrade = {0};
        List<Grade> subjectGrades = gradeDao.getOnSubject(subject);
        subjectGrades.forEach(grade -> averageGrade[0] += grade.getMark());
        return subjectGrades.isEmpty() ? 0 : averageGrade[0] / subjectGrades.size();
    }

    private void validate(LocalDate date, Subject subject, List<Grade> grades) throws AddingGradeException {
        if(date.isBefore(LocalDate.now().withDayOfMonth(1).withDayOfYear(1))){
            throw new AddingGradeException("Grade date can not be before beginning of the year");
        }
        if(date.isAfter(LocalDate.now())){
            throw new AddingGradeException("Grade date can not be after today");
        }
        if(isAlreadyGraded(subject, grades)){
            throw new AddingGradeException("There can not be two grades on the same subject on the same day");
        }
    }

    private boolean isAlreadyGraded(Subject subject/*, LocalDate date*/, List<Grade> grades) {
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
