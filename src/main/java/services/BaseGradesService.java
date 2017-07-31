package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import exceptions.AddingGradeException;
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
    final static String PAST_YEAR_GRADE_MSG = "Grade's date can not be before beginning of the year";
    private static final Type REVIEW_TYPE = new TypeToken<List<Grade>>() {}.getType();

    public abstract void addGrade(Grade addedGrade) throws AddingGradeException;
    public abstract List<Grade> fetchAllGrades();
    public abstract List<Grade> fetchBySubject(long subjectId, boolean ascendingByDate);
    public abstract List<Grade> fetchByDate(LocalDate date);
    public abstract List<Subject> fetchAllSubjects();
    public abstract Subject fetchSubject(long id);
    public abstract void deleteGrade(long id);
    public abstract void deleteAll();
    public abstract double calculateAvgGrade(long subject);
    public abstract boolean isGraded(Subject subject, LocalDate date);
    public abstract void reloadFromFile(String fileName) throws IOException;
    public abstract void dumpToFile(String fileName) throws IOException;

    void validateDate(LocalDate validatedDate) throws AddingGradeException {
        if(validatedDate.getYear() < LocalDate.now().getYear()){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(validatedDate.isAfter(LocalDate.now())){
            throw new AddingGradeException(AFTER_TODAY_GRADE_MSG);
        }
    }

    public List<Grade> readFromFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (
                FileReader fileReader = new FileReader(fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ){
            return gson.fromJson(jsonReader, REVIEW_TYPE);
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

}
