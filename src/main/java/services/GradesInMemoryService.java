package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
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
    private static final Type REVIEW_TYPE = new TypeToken<List<Grade>>() {}.getType();
    private final String fileName;

    public GradesInMemoryService(String fileName) {
        this.fileName = fileName;
        reloadFromFile();
    }

    @Override
    public void addGrade(Grade addedGrade) throws AddingGradeException {

        Subject subject = addedGrade.getSubject();
        LocalDate date = addedGrade.getDate();

        validateDate(date);

        if(!isGraded(subject, date)){
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
    public List<Grade> fetchBySubject(Subject subject, boolean ascendingByDate) {
        List<Grade> gradesOnDate = retrieveGradesBySubject(subject, this.grades);
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
    public double calculateAvgGrade(Subject subject) {
        final double[] averageGrade = {0};
        List<Grade> subjectGrades = retrieveGradesBySubject(subject, this.grades);
        subjectGrades.forEach(grade -> averageGrade[0] += grade.getMark());
        return subjectGrades.isEmpty() ? 0 : averageGrade[0] / subjectGrades.size();
    }

    public boolean isGraded(Subject subject, LocalDate date) {
        return grades.isEmpty() || !retrieveGradesBySubject(subject, grades).isEmpty();
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    private List<Grade> retrieveGradesBySubject(Subject subject, List<Grade> grades){
        return grades.stream()
                .filter(currentGrade -> currentGrade.getSubject().equals(subject))
                .collect(Collectors.toList());
    }

    private void reloadFromFile() {
        Gson gson = new Gson();
        try (
                FileReader fileReader = new FileReader(this.fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ){
            this.grades = gson.fromJson(jsonReader, REVIEW_TYPE);
        } catch (IOException e) {
            throw new RuntimeException("Exception in reloadFromFile()");
        }
    }

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
