package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class WriterServiceImpl implements WriterService {

    WriterRepository writerRepository;

    @Override
    public Writer save(Writer writer) throws SQLException {
        return writerRepository.save(writer);
    }

    @Override
    public Optional<Writer> findById(Long id) throws SQLException {
        return writerRepository.findById(id);
    }

    @Override
    public List<Writer> findAll() throws SQLException {
        return writerRepository.findAll();
    }

    @Override
    public Optional<Writer> update(Writer writer) throws SQLException {
        return writerRepository.update(writer);
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        return writerRepository.deleteById(id);
    }

    @Override
    public Class<Writer> getEntityClass() {
        return Writer.class;
    }
}
