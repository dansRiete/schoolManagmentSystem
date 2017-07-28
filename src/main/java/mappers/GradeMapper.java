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
            long subject_id = ((Subject) map.get("id")).getId();
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

    @Insert("INSERT INTO " + TABLE_NAME + " (date, subject_id, mark) VALUES (#{date}, #{subject.id}, #{mark})")
    void create(Grade entity);

    @Update("UPDATE " + TABLE_NAME + " SET date = #{date}, subject_id = #{subject.id}, mark = #{mark} WHERE id = #{id}")
    void update(Grade entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(Grade entity);

    @Delete("DELETE FROM " + TABLE_NAME)
    void deleteAll();

    //todo Provider
    @SelectProvider(type = SqlProvider.class, method = "getOrderedByDate")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnSubject(@Param("id") Subject requestedSubject, @Param("ascending") boolean ascending);

    @Select("SELECT avg(grades.mark) FROM grades WHERE subject_id = #{id}")
    Double averageGrade(Subject requestedSubject);

    @Select("SELECT CASE WHEN (SELECT count(*) FROM grades WHERE subject_id = #{subject.id} AND date = #{date} ) > 0 THEN TRUE ELSE FALSE END")
    Boolean isGraded(@Param("subject") Subject subject, @Param("date")LocalDate date);

}
