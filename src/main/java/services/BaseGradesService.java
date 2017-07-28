package services;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import exceptions.AddingGradeException;
import model.Grade;
import model.Subject;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Aleks on 28.07.2017.
 */
public abstract class BaseGradesService {

    final static String TWO_GRADES_ON_DAY_MSG = "There can not be two grades on the same subject on the same day";
    private final static String AFTER_TODAY_GRADE_MSG = "Grade date can not be after today";
    private final static String PAST_YEAR_GRADE_MSG = "Grade date can not be before beginning of the year";

    abstract void addGrade(Grade addedGrade) throws AddingGradeException;
    abstract List<Grade> fetchAllGrades();
    abstract List<Grade> fetchBySubject(Subject subject, boolean ascendingByDate);
    abstract List<Grade> fetchByDate(LocalDate date);
    abstract List<Subject> fetchAllSubjects();
    abstract double calculateAvgGrade(Subject subject);
    abstract boolean isGraded(Subject subject, LocalDate date);

    void validateDate(LocalDate date) throws AddingGradeException {
        if(date.isBefore(LocalDate.now().withDayOfMonth(1).withDayOfYear(1))){
            throw new AddingGradeException(PAST_YEAR_GRADE_MSG);
        }
        if(date.isAfter(LocalDate.now())){
            throw new AddingGradeException(AFTER_TODAY_GRADE_MSG);
        }
    }

    public static String represent(Grade gradeToPrint) {
        return "Subject: " + gradeToPrint.getSubject() + ", Date: " + gradeToPrint.getDate() + ", Mark: " + gradeToPrint.getMark();
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
