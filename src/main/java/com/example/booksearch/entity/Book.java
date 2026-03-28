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

  @Column
  private Integer publish_year;



    // NEW FIELDS (must match DB columns)
    @Column(name = "cover_url")
    private String coverUrl;
    private String description;
    private String subjects;
    private String isbn;

    // Required by JPA
    public Book() {
    }

    // Constructor for creating new books (no ID)
    public Book(String title,
                String author,
                Integer publish_year,
                String coverUrl,
                String description,
                String subjects, String isbn) {
        this.title = title;
        this.author = author;
        this.publish_year = publish_year;
        this.coverUrl = coverUrl;
        this.description = description;
        this.subjects = subjects;
        this.isbn = isbn;
    }

    //  GETTERS (needed for JSON output)
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() {return isbn;}
    public String getSubjects() {return subjects;}
    public String getDescription() {return description;}
    public String getCoverUrl() {return coverUrl;}
    public Integer getPublish_year() {return publish_year;}
    //  SETTERS (needed for JSON -> Java input)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) {this.isbn = isbn;}
    public void setSubjects(String subjects) {this.subjects = subjects;}
    public void setDescription(String description) {this.description = description;}
    public void setCoverUrl(String coverUrl) {this.coverUrl = coverUrl;}
    public void setPublish_year(Integer publish_year) {this.publish_year = publish_year;}

}
