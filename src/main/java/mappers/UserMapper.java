package mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Aleks on 03.08.2017.
 */
public interface UserMapper {

    @Select("SELECT CASE WHEN (SELECT count(*) FROM users WHERE username = #{username} AND password = #{password}) = 0 THEN FALSE ELSE TRUE END")
    boolean isExists(@Param("username") String username, @Param("password") String password);

}
