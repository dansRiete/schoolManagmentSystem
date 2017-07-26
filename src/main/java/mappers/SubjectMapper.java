package mappers;

import model.Subject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface SubjectMapper {

    String TABLE_NAME = "subjects";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE id = #{id}")
    Subject getById(long id);

    @Select("SELECT * FROM " + TABLE_NAME + "")
    List<Subject> getAll();

    @Insert("INSERT INTO " + TABLE_NAME + " (title) VALUES (#{title})")
    void create(Subject entity);

    @Update("UPDATE " + TABLE_NAME + " SET title = #{title} WHERE subjects.id = #{id}")
    void update(Subject entity);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void delete(Subject entity);
}
