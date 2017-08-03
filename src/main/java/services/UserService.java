package services;

import dao.UserDao;
import datasources.DataSource;

/**
 * Created by Aleks on 03.08.2017.
 */
public class UserService {
    UserDao userDao = new UserDao(DataSource.getSqlSessionFactory());
    public boolean isExists(String username, String password){
        return userDao.isExists(username, password);
    }
}
