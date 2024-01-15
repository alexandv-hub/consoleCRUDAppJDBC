package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Label;

import java.util.Optional;

public interface LabelService extends GenericEntityService<Label> {

    Optional<Label> findByName(String labelName);

}
