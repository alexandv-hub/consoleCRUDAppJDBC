package com.consoleCRUDApp.repository;

import com.consoleCRUDApp.model.Label;

import java.util.Optional;

public interface LabelRepository extends GenericRepository<Label, Long> {

    boolean isLabelExistInRepository(Label entity);

    Optional<Label> getLabelByName(String labelName);
}
