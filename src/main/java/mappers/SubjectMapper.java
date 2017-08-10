package mappers;

import model.Subject;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface SubjectMapper {

    String TABLE_NAME = "subjects";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE id = #{id}")
    Subject getById(long id);

    @Select("SELECT * FROM " + TABLE_NAME)
    List<Subject> getAll();

    @Insert("INSERT INTO " + TABLE_NAME + " (title) VALUES (#{title})")
    void create(Subject entity);

    @Insert({"<script>",
            "INSERT INTO "+TABLE_NAME+" (title) values ",
            "<foreach collection='list' item='subj' separator = '),(' open ='(' close=')' >#{subj.title}</foreach>",
            "</script>"})
    void createAll(@Param("list") List<Subject> subjects);

    @Update("UPDATE " + TABLE_NAME + " SET title = #{title} WHERE id = #{id}")
    void update(Subject entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(long id);

    @Delete("DELETE FROM " + TABLE_NAME)
    void deleteAll();

    @Select("SELECT CASE WHEN (SELECT count(*) FROM subjects WHERE title = #{subjectTitle}) > 0 THEN TRUE ELSE FALSE END")
    Boolean isExist(@Param("subjectTitle") String subjectTitle);
}
