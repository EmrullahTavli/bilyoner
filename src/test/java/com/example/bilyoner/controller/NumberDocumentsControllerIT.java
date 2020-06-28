package com.example.bilyoner.controller;

import com.example.bilyoner.base.BaseIntegrationTest;
import com.example.bilyoner.model.document.NumberDocument;
import com.example.bilyoner.model.dto.NumberDto;
import com.example.bilyoner.repository.NumberDocumentRepository;
import com.jayway.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumberDocumentsControllerIT extends BaseIntegrationTest {

    @Autowired
    NumberDocumentRepository numberDocumentRepository;

    @Test
    void postGivenDocument() {
        NumberDto numberDto = new NumberDto();
        numberDto.setNumber(5L);

        given()
                .body(numberDto)
                .contentType(ContentType.JSON)
                .when()
                .post("/numbers")
                .then()
                .statusCode(HttpStatus.OK.value());

        Optional<NumberDocument> numberDocumentOptional = numberDocumentRepository.findByNumber(numberDto.getNumber());

        assertTrue(numberDocumentOptional.isPresent());

        numberDocumentRepository.deleteById(numberDocumentOptional.get().getId());
    }

    @Test
    void getAllDocuments() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        NumberDocument numberDocument2 = new NumberDocument(UUID.randomUUID().toString(), 7L, LocalDateTime.now());

        List<NumberDocument> numberDocuments = Arrays.asList(numberDocument, numberDocument2);
        numberDocumentRepository.saveAll(numberDocuments);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/numbers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].number", Matchers.is(numberDocument.getNumber().intValue()))
                .body("[0].id", Matchers.is(numberDocument.getId()))
                .body("[1].number", Matchers.is(numberDocument2.getNumber().intValue()))
                .body("[1].id", Matchers.is(numberDocument2.getId()));

        numberDocumentRepository.deleteAll(numberDocuments);
    }

    @Test
    void getAllDocuments_orderByDesc() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        NumberDocument numberDocument2 = new NumberDocument(UUID.randomUUID().toString(), 7L, LocalDateTime.now());

        List<NumberDocument> numberDocuments = Arrays.asList(numberDocument, numberDocument2);
        numberDocumentRepository.saveAll(numberDocuments);

        given()
                .contentType(ContentType.JSON)
                .when()
                .queryParam("sortedBy", "desc")
                .get("/numbers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].number", Matchers.is(numberDocument2.getNumber().intValue()))
                .body("[0].id", Matchers.is(numberDocument2.getId()))
                .body("[1].number", Matchers.is(numberDocument.getNumber().intValue()))
                .body("[1].id", Matchers.is(numberDocument.getId()));

        numberDocumentRepository.deleteAll(numberDocuments);
    }

    @Test
    void getDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        numberDocumentRepository.save(numberDocument);

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("number", numberDocument.getNumber())
                .get("/numbers/{number}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("number", Matchers.is(numberDocument.getNumber().intValue()))
                .body("id", Matchers.is(numberDocument.getId()));

        numberDocumentRepository.deleteById(numberDocument.getId());
    }

    @Test
    void getMinDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        NumberDocument numberDocument2 = new NumberDocument(UUID.randomUUID().toString(), 7L, LocalDateTime.now());

        List<NumberDocument> numberDocuments = Arrays.asList(numberDocument, numberDocument2);
        numberDocumentRepository.saveAll(numberDocuments);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/numbers/min")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("number", Matchers.is(numberDocument.getNumber().intValue()))
                .body("id", Matchers.is(numberDocument.getId()));

        numberDocumentRepository.deleteAll(numberDocuments);
    }

    @Test
    void getMaxDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        NumberDocument numberDocument2 = new NumberDocument(UUID.randomUUID().toString(), 7L, LocalDateTime.now());

        List<NumberDocument> numberDocuments = Arrays.asList(numberDocument, numberDocument2);
        numberDocumentRepository.saveAll(numberDocuments);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/numbers/max")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("number", Matchers.is(numberDocument2.getNumber().intValue()))
                .body("id", Matchers.is(numberDocument2.getId()));

        numberDocumentRepository.deleteAll(numberDocuments);
    }

    @Test
    void deleteDocument() {
        NumberDocument numberDocument = new NumberDocument(UUID.randomUUID().toString(), 3L, LocalDateTime.now());
        numberDocumentRepository.save(numberDocument);

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("number", numberDocument.getNumber())
                .delete("/numbers/{number}")
                .then()
                .statusCode(HttpStatus.OK.value());

        Optional<NumberDocument> numberDocumentOptional = numberDocumentRepository.findByNumber(numberDocument.getNumber());

        assertFalse(numberDocumentOptional.isPresent());
    }
}