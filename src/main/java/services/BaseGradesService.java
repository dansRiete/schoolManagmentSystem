package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.*;
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
    public static final Type GRADES_LIST_REVIEW_TYPE = new TypeToken<List<Grade>>() {}.getType();

    public abstract void addGrade(Grade addedGrade) throws AddingGradeException;
    public abstract void addGrades(List<Grade> addedGrades) throws AddingGradeException;
    public abstract void addSubject(String title) throws SubjectIllegalTitleException, SubjectExistsException;
    public abstract void addAllSubjects(List<Subject> subjects);
    public abstract List<Grade> fetchAllGrades();
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
    public abstract void reloadCollectionFromJson(String json) throws IOException;
    public abstract int availablePagesNumber(long requestedSubjectId, LocalDate requestedDate);

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
        if(grades == null || grades.isEmpty()){
            return new ArrayList<>();
        }
        HashSet<Subject> subjects = new HashSet<>();
        grades.forEach(grade -> subjects.add(grade.getSubject()));
        return new ArrayList<>(subjects);
    }

    public String toJson(List<Grade> grades) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(grades);
    }

    public List<Grade> fromJson(String json) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(json, GRADES_LIST_REVIEW_TYPE);
    }


}
