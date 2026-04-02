package com.example.booksearch.controller;


import com.example.booksearch.entity.Book;
import com.example.booksearch.repository.BookRepository;
import com.example.booksearch.service.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class BookSearchController {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private BookSearchService bookSearchService;

    public BookSearchController(BookRepository bookRepository, BookSearchService bookSearchService) {
        this.bookRepository = bookRepository;
        this.bookSearchService = bookSearchService;
    }


    /* does not support pagination
    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
    */

    @GetMapping("/books/searchUnifiede")
    public Page<Book> searchUnifiede(
            @RequestParam String q,
            Pageable pageable
    ) {
        return bookSearchService.searchUnifiede(q, pageable);
    }





    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @GetMapping("/books/search")
    public Page<Book> search(
            @RequestParam String q,
            @RequestParam(required=false) Integer year,
            @PageableDefault(size = 20, sort = "title") Pageable pageable
    ) {
        return bookRepository.search(q, year, pageable);
    }


    @GetMapping("/books/default")
    public Page<Book> defaultBooks(Pageable pageable) {
        return bookRepository.findAllEnriched(pageable);
    }

    @GetMapping("books/recent")
    public Page<Book> recentBooks(Pageable pageable) {
        return bookRepository.findRecent(pageable);
    }

    @GetMapping("/books")
    public Page<Book> booksByAuthor(
            @RequestParam(required=false) String author,
            Pageable pageable
    ) {
        if (author != null) {
            return bookRepository.searchByAuthor(author, pageable);
        }
        return bookRepository.findAllEnriched(pageable);
    }

    @GetMapping("/authors")
    public Page<Map<String, Object>> allAuthor(Pageable pageable) {
        return bookRepository.findAllAuthor(pageable);
    }

    @GetMapping("/authors/top")
    public List<Map<String, Object>> topAuthors(
            @RequestParam(defaultValue="10") int limit
    ) {
        return bookRepository.topAuthors(limit);
    }




}
