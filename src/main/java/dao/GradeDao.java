package dao;

import mappers.GradeMapper;
import model.Grade;
import model.Subject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDate;
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

    public List<Grade> getOnDate(LocalDate requestedDate) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).getOnDate(requestedDate);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    public List<Grade> getOnDateAndSubject(long subjectId, LocalDate requestedDate) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).getOnDateAndSubject(subjectId, requestedDate);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    public List<Grade> getOnSubject(long requestedSubjectId, boolean ascending) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).getOnSubject(requestedSubjectId, ascending);
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
    public void createAll(List<Grade> grades) {
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

    public Double averageGrade(long subject){
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).averageGrade(subject);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    public Boolean isGraded(Subject subject, LocalDate date){
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession.getMapper(GradeMapper.class).isGraded(subject, date);
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
    public void delete(long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.getMapper(GradeMapper.class).delete(id);
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
            sqlSession.getMapper(GradeMapper.class).deleteAll();
            sqlSession.commit();
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
