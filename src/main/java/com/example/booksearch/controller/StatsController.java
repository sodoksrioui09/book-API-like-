package com.example.booksearch.controller;

import com.example.booksearch.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatsController {

    @Autowired
    private final BookRepository bookRepository;
    public StatsController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }



    // 1. TOTAL RAW BOOKS (NOT ENRICHED)
    @GetMapping("/stats/raw")
    public Map<String, Long> getRawStats() {
        long raw = bookRepository.countByCoverUrlIsNull();
        return Map.of("raw", raw);
    }

    // 2. TOTAL ENRICHED BOOKS
    @GetMapping("/stats/enriched")
    public Map<String, Long> getEnrichedStats() {
        long enriched = bookRepository.countByCoverUrlIsNotNull();
        return Map.of("enriched", enriched);
    }

    // 3. TOTAL UNIQUE AUTHORS
    @GetMapping("/stats/authors")
    public Map<String, Long> getAuthorsStats() {
        long authors = bookRepository.countDistinctAuthors();
        return Map.of("count", authors);
    }
}

