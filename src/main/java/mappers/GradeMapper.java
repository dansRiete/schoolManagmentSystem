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
                            " ORDER BY DATE" + (ascending ? "" : " DESC, id");
            return sql;
        }

        public String getOrderedByDateLimit(Map<String, Object> map){
            long subject_id = (Long) map.get("id");
            boolean ascending = ((Boolean) map.get("ascending"));
            int limit = ((Integer) map.get("limit"));
            int offset = ((Integer) map.get("offset"));
            String sql =    "SELECT * FROM " + TABLE_NAME +
                    " WHERE subject_id = "+ subject_id +
                    " ORDER BY DATE" + (ascending ? "" : " DESC") + ", id LIMIT " + limit + " OFFSET " + offset;
            return sql;
        }

        public String deleteInId(Map<String, Object> map){
            List<Long> ids = (List<Long>) map.get("ids");
            String whereClause = ids.toString().replace('[', '(').replace(']', ')');
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE id IN " + whereClause;
            return sql;
        }
    }

    @Select("SELECT id, subject_id, mark, date FROM grades WHERE grades.id = #{id} ORDER BY date DESC, id")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    Grade getById(Long id);

    @Select("SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC, id")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getAll();

    @Select("SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC, id LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getAllLimit(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate} ORDER BY date DESC, id")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDate(LocalDate requestedDate);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate} ORDER BY date DESC, id LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDateLimit(@Param("requestedDate") LocalDate requestedDate, @Param("limit") int limit, @Param("offset") int offset);

    @SelectProvider(type = SqlProvider.class, method = "getOrderedByDate")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnSubject(@Param("id") Long requestedSubjectId, @Param("ascending") boolean ascending);

    @SelectProvider(type = SqlProvider.class, method = "getOrderedByDateLimit")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnSubjectLimit(
            @Param("id") Long requestedSubjectId, @Param("ascending") boolean ascending,
            @Param("limit") int limit, @Param("offset") int offset
    );

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate} AND subject_id = #{id} ORDER BY date DESC, id")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDateAndSubject(@Param("id")long id, @Param("requestedDate")LocalDate requestedDate);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE date = #{requestedDate} AND subject_id = #{id} ORDER BY date DESC, id LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id", one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getOnDateAndSubjectLimit(@Param("id")long id, @Param("requestedDate") LocalDate requestedDate, @Param("limit") int limit, @Param("offset") int offset);

    @Insert("INSERT INTO " + TABLE_NAME + " (date, subject_id, mark) VALUES (#{date}, #{subject.id}, #{mark})")
    void create(Grade entity);

    @Insert({"<script>",
            "INSERT INTO " + TABLE_NAME + " (date, subject_id, mark) values ",
            "<foreach collection='list' item='grd' separator = '),(' open ='(' close=')' >#{grd.date}, #{grd.subject.id}, #{grd.mark}</foreach>",
            "</script>"})
    void createAll(@Param("list")List<Grade> list);

    @Update("UPDATE " + TABLE_NAME + " SET date = #{date}, subject_id = #{subject.id}, mark = #{mark} WHERE id = #{id}")
    void update(Grade entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(long id);

    @SelectProvider(type = SqlProvider.class, method = "deleteInId")
    void deleteInId(@Param("ids") List<Long> ids);

    @Delete("DELETE FROM " + TABLE_NAME)
    void deleteAll();

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

    @Select("SELECT count(*) FROM grades")
    long countAll();

    @Select("SELECT count(*) FROM grades WHERE date = #{requestedDate}")
    long countOnDate(@Param("requestedDate") LocalDate requestedDate);

    @Select("SELECT count(*) FROM grades WHERE subject_id = #{id}")
    long countOnSubject(@Param("id") Long requestedSubjectId);

    @Select("SELECT count(*) FROM grades WHERE subject_id = #{id} AND date = #{requestedDate}")
    long countOnSubjectAndDate(@Param("id")long subjectId, @Param("requestedDate") LocalDate requestedDate);

}
