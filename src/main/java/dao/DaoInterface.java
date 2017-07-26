package dao;

import java.util.List;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface DaoInterface<T, Id> {

    T getById(Id id);
    List<T> getAll();
    void create(T entity);
    void create(List<T> entities);
    void update(T entity);
    void delete (T entity);

}
