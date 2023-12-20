package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.WriterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WriterServiceImplTest {

    @Mock
    private WriterRepository writerRepository;

    @InjectMocks
    private WriterServiceImpl writerService;

    @Test
    void testSaveWriter() throws SQLException {
        Writer writer = Writer.builder().build();
        when(writerRepository.save(writer)).thenReturn(writer);

        Writer savedWriter = writerService.save(writer);

        assertNotNull(savedWriter);
        verify(writerRepository, times(1)).save(writer);
    }

    @Test
    void testFindById() throws SQLException {
        Long id = 1L;
        Optional<Writer> writerOptional = Optional.of(Writer.builder().build());
        when(writerRepository.findById(id)).thenReturn(writerOptional);

        Optional<Writer> foundWriter = writerService.findById(id);

        assertTrue(foundWriter.isPresent());
        verify(writerRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllWriters() throws SQLException {
        List<Writer> writersList = Arrays.asList(Writer.builder().build(), Writer.builder().build());
        when(writerRepository.findAll()).thenReturn(writersList);

        List<Writer> foundWriters = writerService.findAll();

        assertNotNull(foundWriters);
        assertEquals(2, foundWriters.size());
        verify(writerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateWriter() throws SQLException {
        Writer writerToUpdate = Writer.builder().id(1L).build();
        Writer updatedWriter = Writer.builder().id(1L).build(); // Предполагается, что объект обновлен
        when(writerRepository.update(writerToUpdate)).thenReturn(Optional.of(updatedWriter));

        Optional<Writer> result = writerService.update(writerToUpdate);

        assertTrue(result.isPresent());
        assertEquals(updatedWriter, result.get());
        verify(writerRepository, times(1)).update(writerToUpdate);
    }

    @Test
    void testDeleteWriterByIdSuccessfully() throws SQLException {
        Long id = 1L;
        when(writerRepository.deleteById(id)).thenReturn(true);

        boolean isDeleted = writerService.deleteById(id);

        assertTrue(isDeleted);
        verify(writerRepository, times(1)).deleteById(id);
    }

}
