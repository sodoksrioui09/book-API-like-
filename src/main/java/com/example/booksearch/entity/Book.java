package com.example.booksearch.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String author;

    @Column(name = "publication_year")
    private Integer publicationYear;

    // Required by JPA
    public Book() {
    }

    // Constructor for creating new books (no ID)
    public Book(String title, String author, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    //  GETTERS (needed for JSON output)
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Integer getPublicationYear() { return publicationYear; }

    //  SETTERS (needed for JSON -> Java input)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

}
