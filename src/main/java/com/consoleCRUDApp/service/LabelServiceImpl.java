package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label save(Label label) throws SQLException {
        return labelRepository.save(label);
    }

    @Override
    public Optional<Label> findById(Long id) throws SQLException {
        return labelRepository.findById(id);
    }

    @Override
    public List<Label> findAll() throws SQLException {
        return labelRepository.findAll();
    }

    @Override
    public Optional<Label> update(Label label) throws SQLException {
        return labelRepository.update(label);
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        return labelRepository.deleteById(id);
    }

    @Override
    public Class<Label> getEntityClass() {
        return labelRepository.getEntityClass();
    }

    @Override
    public boolean isLabelExistInRepository(Label label) throws SQLException {
        return labelRepository.isLabelExistInRepository(label);
    }

}

