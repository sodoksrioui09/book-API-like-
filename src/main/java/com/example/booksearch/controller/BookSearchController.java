package com.example.booksearch.controller;


import com.example.booksearch.entity.Book;
import com.example.booksearch.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class BookSearchController {

    @Autowired
    private final BookRepository bookRepository;

    public BookSearchController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /* does not support pagnation
    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
    */
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


    @GetMapping("/authors/top")
    public List<Map<String, Object>> topAuthors(
            @RequestParam(defaultValue="10") int limit
    ) {
        return bookRepository.topAuthors(limit);
    }




}
