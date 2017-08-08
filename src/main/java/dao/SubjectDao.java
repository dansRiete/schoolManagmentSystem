package dao;

import mappers.SubjectMapper;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public class SubjectDao implements BaseDao<Subject, Long> {

    private SqlSessionFactory sqlSessionFactory;

    public SubjectDao(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Subject getById(Long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
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
            sqlSession = sqlSessionFactory.openSession();
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
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(SubjectMapper.class).create(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void createAll(List<Subject> subjects) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            SubjectMapper subjectMapper = sqlSession.getMapper(SubjectMapper.class);
            subjects.forEach(subjectMapper::create); //todo
//            subjectMapper.createAll(subjects);
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
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(SubjectMapper.class).update(entity);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void delete(long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(SubjectMapper.class).delete(id);
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public void deleteAll() {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(SubjectMapper.class).deleteAll();
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    public boolean isExists(String subjectTitle){
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(SubjectMapper.class).isExist(subjectTitle);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
