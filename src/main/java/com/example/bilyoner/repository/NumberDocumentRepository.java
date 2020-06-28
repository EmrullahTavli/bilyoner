package com.example.bilyoner.repository;

import com.example.bilyoner.model.document.NumberDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NumberDocumentRepository extends MongoRepository<NumberDocument, String> {
    Optional<NumberDocument> findByNumber(Long number);

    Optional<NumberDocument> findFirstByOrderByNumberDesc();

    Optional<NumberDocument> findFirstByOrderByNumberAsc();
}
