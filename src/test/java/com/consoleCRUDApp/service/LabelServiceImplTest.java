package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.repository.LabelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LabelServiceImplTest {

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    void testSaveLabel() throws SQLException {
        Label label = Label.builder().build();
        when(labelRepository.save(any(Label.class))).thenReturn(label);

        Label savedLabel = labelService.save(label);

        assertNotNull(savedLabel);
        verify(labelRepository).save(label);
    }

    @Test
    void testFindById() throws SQLException {
        Long id = 1L;
        Label label = Label.builder().build();
        when(labelRepository.findById(id)).thenReturn(Optional.of(label));

        Optional<Label> foundLabel = labelService.findById(id);

        assertTrue(foundLabel.isPresent());
        assertEquals(label, foundLabel.get());
        verify(labelRepository).findById(id);
    }

    @Test
    void testFindAll() throws SQLException {
        Label label = Label.builder().build();
        when(labelRepository.findAll()).thenReturn(Collections.singletonList(label));

        List<Label> labels = labelService.findAll();

        assertFalse(labels.isEmpty());
        assertEquals(1, labels.size());
        verify(labelRepository).findAll();
    }

    @Test
    void testUpdateLabel() throws SQLException {
        Label label = Label.builder().build();
        when(labelRepository.update(label)).thenReturn(Optional.of(label));

        Optional<Label> updatedLabel = labelService.update(label);

        assertTrue(updatedLabel.isPresent());
        verify(labelRepository).update(label);
    }

    @Test
    void testDeleteById() throws SQLException {
        Long id = 1L;
        when(labelRepository.deleteById(id)).thenReturn(true);

        boolean isDeleted = labelService.deleteById(id);

        assertTrue(isDeleted);
        verify(labelRepository).deleteById(id);
    }

    @Test
    void testGetEntityClass() {
        when(labelRepository.getEntityClass()).thenReturn(Label.class);

        Class<Label> entityClass = labelService.getEntityClass();

        assertEquals(Label.class, entityClass);
        verify(labelRepository).getEntityClass();
    }

}
