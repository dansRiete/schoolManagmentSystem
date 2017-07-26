package dao;

/**
 * Created by Aleks on 26.07.2017.
 */
public interface DaoInterface<T, Id> {
    T get(Id id);
}
