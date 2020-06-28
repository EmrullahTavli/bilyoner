package com.example.bilyoner.service;

import com.example.bilyoner.exception.NumberDocumentException;
import com.example.bilyoner.model.document.NumberDocument;
import com.example.bilyoner.repository.NumberDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NumberDocumentServiceTest {
    @Mock
    NumberDocumentRepository numberDocumentRepository;
    NumberDocumentService numberDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        numberDocumentService = new NumberDocumentService(numberDocumentRepository);
    }

    @Test
    void findAllDocuments() {
        NumberDocument numberDocument1 = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        NumberDocument numberDocument2 = new NumberDocument(UUID.randomUUID().toString(), 5L, LocalDateTime.now());

        List<NumberDocument> numberDocumentList = Arrays.asList(numberDocument1, numberDocument2);

        when(numberDocumentRepository.findAll(ArgumentMatchers.any(Sort.class)))
                .thenReturn(numberDocumentList);

        List<NumberDocument> allDocuments = numberDocumentService.findAllDocuments("asc");
        assertEquals(numberDocumentList, allDocuments);
    }

    @Test
    void findDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 5L, LocalDateTime.now());

        when(numberDocumentRepository.findByNumber(numberDocument.getNumber()))
                .thenReturn(Optional.of(numberDocument));

        NumberDocument numberDoc = numberDocumentService.findDocument(numberDocument.getNumber());
        assertEquals(numberDocument, numberDoc);
    }

    @Test
    void findDocument_shouldFail() {
        long number = 5L;

        when(numberDocumentRepository.findByNumber(number))
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> numberDocumentService.findDocument(5L));
        assertNotNull(throwable);
        assertTrue(throwable instanceof NumberDocumentException);
        assertEquals("Given number is not found.", throwable.getMessage());
    }

    @Test
    void findMaxDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 5L, LocalDateTime.now());

        when(numberDocumentRepository.findFirstByOrderByNumberDesc())
                .thenReturn(Optional.of(numberDocument));

        NumberDocument numberDoc = numberDocumentService.findMaxDocument();
        assertEquals(numberDocument, numberDoc);
    }

    @Test
    void findMaxDocument_shouldFail() {
        when(numberDocumentRepository.findFirstByOrderByNumberDesc())
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> numberDocumentService.findMaxDocument());
        assertNotNull(throwable);
        assertTrue(throwable instanceof NumberDocumentException);
        assertEquals("Repository is empty.", throwable.getMessage());
    }

    @Test
    void findMinDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 5L, LocalDateTime.now());

        when(numberDocumentRepository.findFirstByOrderByNumberAsc())
                .thenReturn(Optional.of(numberDocument));

        NumberDocument numberDoc = numberDocumentService.findMinDocument();
        assertEquals(numberDocument, numberDoc);
    }

    @Test
    void findMinDocument_noDocFound_shouldFail() {
        when(numberDocumentRepository.findFirstByOrderByNumberAsc())
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> numberDocumentService.findMinDocument());
        assertNotNull(throwable);
        assertTrue(throwable instanceof NumberDocumentException);
        assertEquals("Repository is empty.", throwable.getMessage());
    }

    @Test
    void deleteDocument_shouldPass() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 5L, LocalDateTime.now());

        when(numberDocumentRepository.findByNumber(numberDocument.getNumber()))
                .thenReturn(Optional.of(numberDocument));

        numberDocumentService.deleteDocument(numberDocument.getNumber());

        verify(numberDocumentRepository, times(1)).deleteById(numberDocument.getId());

    }

    @Test
    void deleteDocument_numberNotFound_shouldFail() {
        long number = 5L;

        when(numberDocumentRepository.findByNumber(number))
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> numberDocumentService.deleteDocument(5L));
        assertNotNull(throwable);
        assertTrue(throwable instanceof NumberDocumentException);
        assertEquals("Given number is not found.", throwable.getMessage());

        verify(numberDocumentRepository, never()).deleteById(anyString());
    }
}
