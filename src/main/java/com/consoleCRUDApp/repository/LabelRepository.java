package com.consoleCRUDApp.repository;

import com.consoleCRUDApp.model.Label;

public interface LabelRepository extends GenericRepository<Label, Long> {

    boolean isLabelExistInRepository(Label entity);

    Long getIdByName(String labelName);
}
