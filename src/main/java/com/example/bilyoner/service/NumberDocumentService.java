package com.example.bilyoner.service;

import com.example.bilyoner.exception.NumberDocumentException;
import com.example.bilyoner.model.document.NumberDocument;
import com.example.bilyoner.repository.NumberDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NumberDocumentService {
    private final NumberDocumentRepository numberDocumentRepository;

    public List<NumberDocument> findAllDocuments(String orderBy) {
        Sort.Direction orderByDefault = Sort.Direction.ASC;
        if (!StringUtils.isEmpty(orderBy) && orderBy.equals("desc")) {
            orderByDefault = Sort.Direction.DESC;
        } else if (!StringUtils.isEmpty(orderBy) && !orderBy.equals("asc")) {
            throw new NumberDocumentException("Given parameter value is invalid.");
        }
        return numberDocumentRepository.findAll(Sort.by(orderByDefault, "number"));
    }

    public void saveDocument(Long number) {
        Optional<NumberDocument> numberOptional = numberDocumentRepository.findByNumber(number);
        if (numberOptional.isPresent()) {
            throw new NumberDocumentException("Given number is already exist.");
        }
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), number, LocalDateTime.now());
        numberDocumentRepository.insert(numberDocument);
    }

    public NumberDocument findMaxDocument() {
        return numberDocumentRepository.findFirstByOrderByNumberDesc()
                .orElseThrow(() -> new NumberDocumentException("Repository is empty."));
    }

    public NumberDocument findMinDocument() {
        return numberDocumentRepository.findFirstByOrderByNumberAsc()
                .orElseThrow(() -> new NumberDocumentException("Repository is empty."));
    }

    public NumberDocument findDocument(Long number) {
        return numberDocumentRepository.findByNumber(number)
                .orElseThrow(() -> new NumberDocumentException("Given number is not found."));
    }

    public void deleteDocument(Long number) {
        NumberDocument numberDocument = numberDocumentRepository.findByNumber(number)
                .orElseThrow(() -> new NumberDocumentException("Given number is not found."));

        numberDocumentRepository.deleteById(numberDocument.getId());

    }
}
