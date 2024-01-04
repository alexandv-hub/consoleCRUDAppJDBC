package com.consoleCRUDApp.service.impl;

import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import com.consoleCRUDApp.service.WriterService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class WriterServiceImpl implements WriterService {

    WriterRepository writerRepository;

    @Override
    public Optional<Writer> save(Writer writer) {
        return writerRepository.save(writer);
    }

    @Override
    public Optional<Writer> findById(Long id) {
        return writerRepository.findById(id);
    }

    @Override
    public List<Writer> findAll() {
        return writerRepository.findAll();
    }

    @Override
    public Optional<Writer> update(Writer writer) {
        return writerRepository.update(writer);
    }

    @Override
    public boolean deleteById(Long id) {
        return writerRepository.deleteById(id);
    }
}
