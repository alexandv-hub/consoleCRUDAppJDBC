package com.consoleCRUDApp.service.impl;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.service.LabelService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Optional<Label> save(Label label) {
        return labelRepository.save(label);
    }

    @Override
    public Optional<Label> findById(Long id) {
        return labelRepository.findById(id);
    }

    @Override
    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    @Override
    public Optional<Label> update(Label label) {
        return labelRepository.update(label);
    }

    @Override
    public boolean deleteById(Long id) {
        return labelRepository.deleteById(id);
    }

    @Override
    public boolean isLabelExistInRepository(Label label) {
        return labelRepository.isLabelExistInRepository(label);
    }

}

