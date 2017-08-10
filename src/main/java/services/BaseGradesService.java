package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.AddingGradeException;
import exceptions.AddingSubjectException;
import exceptions.DeletingSubjectException;
import exceptions.NoGradesException;
import model.Grade;
import model.Subject;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
public abstract class BaseGradesService {

    final static String TWO_GRADES_ON_DAY_MSG = "There can not be more than one grade on the same subject on the same day";
    final static String NEGATIVE_MARK_MSG = "Mark can not be negative value";
    final static String AFTER_TODAY_GRADE_MSG = "Grade's date can not be after today";
    final static String DATE_CAN_NOT_BE_NULL_MSG = "Date can not be null";
    final static String PAST_YEAR_GRADE_MSG = "Grade's date can not be before beginning of the year";
    public static final Type GRADES_LIST_REVIEW_TYPE = new TypeToken<List<Grade>>() {}.getType();

    public abstract void addGrade(Grade addedGrade) throws AddingGradeException;
    public abstract void addGrades(List<Grade> addedGrades) throws AddingGradeException;
    public abstract void addSubject(String title) throws AddingSubjectException;
    public abstract List<Grade> fetchGrades(long subjectId, LocalDate date);
    public abstract List<Grade> fetchGrades(long subjectId, LocalDate date, int page);
    public abstract List<Subject> fetchAllSubjects();
    public abstract Subject fetchSubject(long id);
    public abstract void deleteGrade(long gradeId) throws DeletingSubjectException;
    public abstract void deleteSubject(long subjectId) throws DeletingSubjectException;
    public abstract void forceDeleteSubject(long subjectId);
    public abstract void forceDeleteAllSubjects();
    public abstract void deleteAllGrades();
    public abstract double calculateAvgGrade(long subjectId, LocalDate selectedDate) throws NoGradesException;
    public abstract boolean isGraded(Subject subject, LocalDate date);
    public abstract boolean isSubjectExists(String subjectTitle);
    public abstract void fromJson(String json) throws IOException;
    public abstract String toJson() throws IOException;

    void validateDate(LocalDate validatedDate) throws AddingGradeException {
        if(validatedDate == null){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(validatedDate.getYear() < LocalDate.now().getYear()){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(validatedDate.isAfter(LocalDate.now())){
            throw new AddingGradeException(AFTER_TODAY_GRADE_MSG);
        }
    }

    public List<Subject> extractSubjects(List<Grade> grades){
        HashSet<Subject> subjects = new HashSet<>();
        System.out.println(grades);
        grades.forEach(grade -> subjects.add(grade.getSubject()));
        return new ArrayList<>(subjects);
    }

    public List<Grade> readFromFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (
                FileReader fileReader = new FileReader(fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ){
            return gson.fromJson(jsonReader, GRADES_LIST_REVIEW_TYPE);
        }
    }



    public String readStringFromFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (
                FileReader fileReader = new FileReader(fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ){
            return gson.fromJson(jsonReader, GRADES_LIST_REVIEW_TYPE);
        }
    }

    public void saveToFile(String fileName, List<Grade> gradesToWrite) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(gradesToWrite, writer);
        }
    }

    public static String represent(Grade gradeToPrint) {
        return  "Subject: " + gradeToPrint.getSubject() +
                ", Date: " + gradeToPrint.getDate() +
                ", Mark: " + gradeToPrint.getMark();
    }

    public static String represent(List<Grade> gradesToPrint) {
        StringBuilder gradesRepresent = new StringBuilder();
        for(int i = 0; i < gradesToPrint.size(); i++){
            gradesRepresent.append(represent(gradesToPrint.get(i)));
            if(i != gradesToPrint.size()){
                gradesRepresent.append('\n');
            }
        }
        return gradesRepresent.toString();
    }

    public abstract void toJson(List<Grade> grades) throws IOException;
}
