package dao;

import mappers.GradeMapper;
import mappers.GradeMapper;
import model.Grade;
import model.Grade;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import services.MyBatisService;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public class GradeDao implements DaoInterface <Grade, Long>{

    private SqlSessionFactory sqlSessionFactory;

    public GradeDao(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Grade getById(Long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).getById(id);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public List<Grade> getAll() {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).getAll();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void create(Grade entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(GradeMapper.class).create(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void create(List<Grade> grades) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            GradeMapper gradeMapper = sqlSession.getMapper(GradeMapper.class);
            grades.forEach(gradeMapper::create);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void update(Grade entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(GradeMapper.class).update(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void delete(Grade entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(GradeMapper.class).delete(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
