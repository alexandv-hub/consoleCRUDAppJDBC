package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.DBEntity;

import java.util.List;
import java.util.Optional;

public interface GenericEntityService <T extends DBEntity> {

    Optional<T> save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    Optional<T> update(T entity);
    boolean deleteById(Long id);

}
