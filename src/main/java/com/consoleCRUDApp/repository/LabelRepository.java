package com.consoleCRUDApp.repository;

import com.consoleCRUDApp.model.Label;

import java.util.Optional;

public interface LabelRepository extends GenericRepository<Label, Long> {

    Optional<Label> findByName(String labelName);

}
