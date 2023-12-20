package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericEntityService <T extends Entity> {

    T save(T entity) throws SQLException;
    Optional<T> findById(Long id) throws SQLException;
    List<T> findAll() throws SQLException;
    Optional<T> update(T entity) throws SQLException;
    boolean deleteById(Long id) throws SQLException;

    Class<T> getEntityClass();
}
