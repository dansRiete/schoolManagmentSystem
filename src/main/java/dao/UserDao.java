package dao;

import mappers.GradeMapper;
import mappers.UserMapper;
import model.Grade;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Created by Aleks on 03.08.2017.
 */
public class UserDao {

    private SqlSessionFactory sqlSessionFactory;

    public UserDao(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
    public boolean isExists(String username, String password) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(UserMapper.class).isExists(username, password);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

}
