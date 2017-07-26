package mappers;

import model.Grade;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface GradeMapper {

    String TABLE_NAME = "grades";

    @Select("select id, subject_id, mark, date from grades where grades.id = #{id}")
    @Results({
            @Result(id=true, property = "id", column = "subject_id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id",
            one=@One(select="mappers.SubjectMapper.getById"))
    })
    Grade getById(Long id);

    @Select("SELECT * FROM " + TABLE_NAME + "")
    @Results({
            @Result(id=true, property = "id", column = "subject_id"),
            @Result(property = "mark", column = "mark"),
            @Result(property = "date", column = "date"),
            @Result(property="subject", column="subject_id",
                    one=@One(select="mappers.SubjectMapper.getById"))
    })
    List<Grade> getAll();

    @Insert("INSERT INTO " + TABLE_NAME + " (date, subject_id, mark) VALUES (#{date}, #{subject.id}, #{mark})")
    void create(Grade entity);

    @Update("UPDATE " + TABLE_NAME + " SET date = #{date}, subject_id = #{subject.id}, mark = #{mark} WHERE id = #{id}")
    void update(Grade entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(Grade entity);


}
