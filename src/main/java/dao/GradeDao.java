package dao;

import model.Grade;
import org.apache.ibatis.session.SqlSession;
import services.MyBatisService;

/**
 * Created by Aleks on 26.07.2017.
 */
public class GradeDao implements DaoInterface <Grade, Long>{

    private final static String GRADEMAPPER_SELECT = "GradeMapper.selectGrade";

    @Override
    public Grade get(Long id) {
        SqlSession sqlSession = null;
        try{
            sqlSession = MyBatisService.getSqlSessionFactory().openSession();
            return sqlSession.selectOne(GRADEMAPPER_SELECT, id);
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }
}
