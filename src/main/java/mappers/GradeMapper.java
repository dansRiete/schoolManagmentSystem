package mappers;

import model.Grade;
import model.Subject;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface GradeMapper {

    String TABLE_NAME = "grades";

    class SqlProvider{
        public String getOrderedByDate(Map<String, Object> map){
            long subject_id = (Long) map.get("id");
            boolean ascending = ((Boolean) map.get("ascending"));
            String sql =    "SELECT * FROM " + TABLE_NAME +
                            " WHERE subject_id = "+ subject_id +
                            " ORDER BY DATE" + (ascending ? "" : " DESC");
            return sql;
        }
    }

    @Select("select id, subject_id, mark, date from grades where grades.id = #{id}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    Grade getById(Long id);

    @Select("SELECT * FROM " + TABLE_NAME)
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getAll();

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDate(LocalDate requestedDate);



    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate} AND subject_id = #{id}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDateAndSubject(@Param("id")long id, @Param("requestedDate")LocalDate requestedDate);

    @Insert("INSERT INTO " + TABLE_NAME + " (date, subject_id, mark) VALUES (#{date}, #{subject.id}, #{mark})")
    void create(Grade entity);

    @Update("UPDATE " + TABLE_NAME + " SET date = #{date}, subject_id = #{subject.id}, mark = #{mark} WHERE id = #{id}")
    void update(Grade entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(long id);

    @Delete("DELETE FROM " + TABLE_NAME)
    void deleteAll();

    @SelectProvider(type = SqlProvider.class, method = "getOrderedByDate")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnSubject(@Param("id") Long requestedSubjectId, @Param("ascending") boolean ascending);

    @Select("SELECT avg(grades.mark) FROM grades")
    Double averageGradeOfAll();

    @Select("SELECT avg(grades.mark) FROM grades WHERE subject_id = #{requestedSubject}")
    Double averageGradeBySubject(long requestedSubject);

    @Select("SELECT avg(grades.mark) FROM grades WHERE date = #{date}")
    Double averageGradeByDate(@Param("date") LocalDate requestedDate);

    @Select("SELECT avg(grades.mark) FROM grades WHERE subject_id = #{subjectId} AND date = #{date}")
    Double averageGradeBySubjectAndDate(@Param("subjectId") long requestedSubject, @Param("date")LocalDate selectedDate);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades WHERE subject_id = #{subject.id} AND date = #{date} ) > 0 THEN TRUE ELSE FALSE END")
    Boolean isGraded(@Param("subject") Subject subject, @Param("date")LocalDate date);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades WHERE date = #{date} ) > 0 THEN TRUE ELSE FALSE END")
    Boolean areAnyGradesOnDate(@Param("date") LocalDate date);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades WHERE subject_id = #{subject} ) > 0 THEN TRUE ELSE FALSE END")
    Boolean areAnyGradesOnSubject(@Param("subject") long subject);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades WHERE subject_id = #{subject} AND date = #{date}) > 0 THEN TRUE ELSE FALSE END")
    Boolean areAnyGradesOnDateAndSubject(@Param("subject") long subject, @Param("date") LocalDate date);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades) > 0 THEN TRUE ELSE FALSE END")
    Boolean areAnyGrades();

}
