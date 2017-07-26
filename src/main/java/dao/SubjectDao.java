package dao;

import mappers.GradeMapper;
import mappers.SubjectMapper;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import services.MyBatisService;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public class SubjectDao implements DaoInterface<Subject, Long> {

    @Override
    public Subject getById(Long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            return sqlSession.getMapper(SubjectMapper.class).getById(id);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public List<Subject> getAll() {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            return sqlSession.getMapper(SubjectMapper.class).getAll();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void create(Subject entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            sqlSession.getMapper(SubjectMapper.class).create(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void create(List<Subject> subjects) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            SubjectMapper subjectMapper = sqlSession.getMapper(SubjectMapper.class);
            subjects.forEach(subjectMapper::create);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void update(Subject entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            sqlSession.getMapper(SubjectMapper.class).update(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void delete(Subject entity) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            sqlSession.getMapper(SubjectMapper.class).delete(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
