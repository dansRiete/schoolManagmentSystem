package dao;

import model.Subject;
import org.apache.ibatis.session.SqlSession;
import services.MyBatisService;

/**
 * Created by Aleks on 26.07.2017.
 */
public class SubjectDao implements DaoInterface<Subject, Long> {

    private final static String SUBJECTMAPPER_SELECT = "SubjectMapper.selectSubject";


    @Override
    public Subject get(Long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            return sqlSession.selectOne(SUBJECTMAPPER_SELECT, id);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
