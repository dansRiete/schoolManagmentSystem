package datasources;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aleks on 26.07.2017.
 */
@SuppressWarnings("Duplicates")
public class DataSourceTest {
    private static SqlSessionFactory sqlSessionFactory;
    private static Logger logger = Logger.getLogger(DataSourceTest.class);

    private static void initFactory() throws IOException {
        String resource = "mybatis-config-test.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if(sqlSessionFactory == null){
            try {
                initFactory();
            } catch (IOException e) {
                logger.fatal("There was an error during initializing session factory. " + e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return sqlSessionFactory;
    }
}
