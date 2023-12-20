package com.consoleCRUDApp.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    T save(T entity) throws SQLException;
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    Optional<T> update(T entity) throws SQLException;
    boolean deleteById(ID id) throws SQLException;

    Class<T> getEntityClass();

}
