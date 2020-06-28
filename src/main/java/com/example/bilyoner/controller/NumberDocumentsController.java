package com.example.bilyoner.controller;

import com.example.bilyoner.model.document.NumberDocument;
import com.example.bilyoner.model.dto.NumberDto;
import com.example.bilyoner.service.NumberDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/numbers")
public class NumberDocumentsController {
    private final NumberDocumentService numberDocumentService;

    @PostMapping
    public void postGivenDocument(@RequestBody NumberDto number) {
        numberDocumentService.saveDocument(number.getNumber());
    }

    @GetMapping
    public List<NumberDocument> getAllDocuments(@RequestParam(value = "sortedBy", required = false) String sortedBy) {
        return numberDocumentService.findAllDocuments(sortedBy);
    }

    @GetMapping("/{id}")
    public NumberDocument getDocumentById(@PathVariable Long id) {
        return numberDocumentService.findDocument(id);
    }

    @GetMapping("/max")
    public NumberDocument getMaxDocument() {
        return numberDocumentService.findMaxDocument();
    }

    @GetMapping("/min")
    public NumberDocument getMinDocument() {
        return numberDocumentService.findMinDocument();
    }

    @DeleteMapping("/{id}")
    public void deleteDocumentById(@PathVariable Long id) {
        numberDocumentService.deleteDocument(id);
    }
}
