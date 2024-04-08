package dev.annyni.repository;

import java.util.List;

/**
 * todo Document type GenericRepository
 */
public interface GenericRepository<T, ID>{
    T findById(ID id);

    List<T> findAll();

    T save(T t);

    void deleteById(ID id);

    T update(T t);
}
