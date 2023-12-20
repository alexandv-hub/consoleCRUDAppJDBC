package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Label;

import java.sql.SQLException;

public interface LabelService extends GenericEntityService<Label> {

    boolean isLabelExistInRepository(Label label) throws SQLException;

}
