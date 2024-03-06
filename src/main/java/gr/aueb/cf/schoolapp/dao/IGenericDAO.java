package gr.aueb.cf.schoolapp.dao;

import java.util.List;
import java.util.Map;

/**
 * @param <T>
 */
public interface IGenericDAO<T> {

    T insert(T t);
    T update(T t);
    void delete(Object id);
    T getById(Object id);
    List<T> getAll();
    List<? extends T> getByCriteria(Map<String, Object> criteria);
    <K extends T> List<K> getByCriteria(Class<K> clazz, Map<String, Object> criteria);
}
