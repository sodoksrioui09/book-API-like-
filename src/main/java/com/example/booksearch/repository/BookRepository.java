
package com.example.booksearch.repository;


import com.example.booksearch.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    ///suggest titles => Substring match
    @Query(
            value = """
        SELECT DISTINCT title
        FROM books
        WHERE title ILIKE CONCAT('%', :q, '%')
        ORDER BY title ASC
        LIMIT :limit
    """,
            nativeQuery = true
    )
    List<String> suggestTitles(
            @Param("q") String q,
            @Param("limit") int limit
    );


    ///prefix candidate pool for fuzzy
    @Query(
            value = """
        SELECT DISTINCT title
        FROM books
        WHERE title ILIKE CONCAT(:prefix, '%')
        ORDER BY title ASC
    """,
            nativeQuery = true
    )
    List<String> findTitlesByPrefix(
            @Param("prefix") String prefix
    );




///NotPageable TEXT SEARCH needed for unified search
    @Query(
            value = """                 
            SELECT * FROM books
                        WHERE cover_url IS NOT NULL
                          AND search_vector @@ plainto_tsquery(:q)
                          AND (:year IS NULL OR publication_year = :year)
                        ORDER BY title ASC
                        """,nativeQuery = true)
    List<Book> searchNotPageable(@Param("q") String q, @Param("year") Integer year);
///NotPageable Author SEARCH needed for unified search
    @Query(
            value = """
            SELECT * FROM books
            WHERE cover_url IS NOT NULL
              AND UPPER(author) LIKE UPPER(CONCAT('%', :author, '%'))
            ORDER BY title ASC
            """,nativeQuery = true)
    List<Book> searchByAuthorNotPageable(@Param("author") String author);





    @Query(
            value = """                 
            SELECT * FROM books
                        WHERE cover_url IS NOT NULL
                          AND search_vector @@ plainto_tsquery(:q)
                          AND (:year IS NULL OR publication_year = :year)
                        ORDER BY title ASC
                        """,
            countQuery = """
            SELECT count(*)
            FROM books
            WHERE cover_url IS NOT NULL
                AND search_vector @@ plainto_tsquery(:q)
                AND (:year IS NULL OR publication_year = :year)                                               
            """,
            nativeQuery = true
    )
    Page<Book> search(
            @Param("q") String q,
            @Param("year") Integer year,
            Pageable pageable
    );

    @Query(
            value = """
            SELECT * FROM books
            WHERE cover_url IS NOT NULL
              AND UPPER(author) LIKE UPPER(CONCAT('%', :author, '%'))
            ORDER BY title ASC
            """,
            countQuery = """
            SELECT count(*)
            FROM books
            WHERE cover_url IS NOT NULL
              AND UPPER(author) LIKE UPPER(CONCAT('%', :author, '%'))
            """,
            nativeQuery = true
    )
    Page<Book> searchByAuthor(
            @Param("author") String author,
            Pageable pageable
    );

    @Query(
            value = """
        SELECT * FROM books
        WHERE cover_url IS NOT NULL
        ORDER BY title ASC
        """,
            countQuery = """
        SELECT count(*) FROM books
        WHERE cover_url IS NOT NULL
        """,
            nativeQuery = true
    )
    Page<Book> findAllEnriched(Pageable pageable);

    ///FIND all enrichment recent books by publich_year
    @Query(
            value = """
            SELECT * FROM books
            WHERE cover_url IS NOT NULL 
            ORDER BY publish_year DESC 
    """,    countQuery = """
        SELECT count(*) FROM books
        WHERE cover_url IS NOT NULL
        """
            ,nativeQuery = true)
   Page<Book> findRecent(Pageable pageable);

///find all authors paginated
    @Query(value = """
        SELECT DISTINCT author FROM books
        ORDER BY author ASC 
""",countQuery = """
        SELECT COUNT(DISTINCT author)
        FROM books
""" ,nativeQuery = true)
   Page<Map<String, Object>> findAllAuthor(Pageable pageable);


    @Query(
            value = """
            SELECT author, COUNT(*) AS count
            FROM books
            GROUP BY author
            ORDER BY count DESC
            LIMIT :limit
        """, nativeQuery = true)

    List<Map<String, Object>> topAuthors(@Param("limit") int limit);


    /// stats methods

    long countByCoverUrlIsNotNull();
    long countByCoverUrlIsNull();

    @Query("SELECT COUNT(DISTINCT b.author) FROM Book b")
    long countDistinctAuthors();



}

