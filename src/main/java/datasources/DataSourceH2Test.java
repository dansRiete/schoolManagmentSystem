package datasources;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aleks on 31.07.2017.
 */
public class DataSourceH2Test {
    private static SqlSessionFactory sqlSessionFactory;

    private static void initFactory() throws IOException {
        String resource = "mybatis-h2.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if(sqlSessionFactory == null){
            try {
                initFactory();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return sqlSessionFactory;
    }
}
