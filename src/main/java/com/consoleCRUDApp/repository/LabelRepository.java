package com.consoleCRUDApp.repository;

import com.consoleCRUDApp.model.Label;

import java.sql.SQLException;

public interface LabelRepository extends GenericRepository<Label, Long> {

    boolean isLabelExistInRepository(Label entity) throws SQLException;

    Long getIdByName(String labelName);
}
