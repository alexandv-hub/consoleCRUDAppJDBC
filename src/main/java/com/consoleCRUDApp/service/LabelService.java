package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Label;

public interface LabelService extends GenericEntityService<Label> {

    boolean isLabelExistInRepository(Label label);

}
