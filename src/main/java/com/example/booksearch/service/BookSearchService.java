package com.example.booksearch.service;

import com.example.booksearch.entity.Book;
import com.example.booksearch.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.tokens.Token;

import org.springframework.data.domain.Pageable;import java.util.*;

@Service
public class BookSearchService {

    @Autowired
    private BookRepository bookRepository;
    public BookSearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }



    public Page<Book> searchUnifiede(String  q, Pageable pageable){

        //initialised arraylist
        List<Book> ListOfSearchByText = new ArrayList<>();
        List<Book> ListOfSearchByAuthor = new ArrayList<>();
        //Fetch unpaged results
        ListOfSearchByText = bookRepository.searchNotPageable(q,null);
        ListOfSearchByAuthor = bookRepository.searchByAuthorNotPageable(q);
            //merge and overwrite duplicates using LinkedHashMap
            Map<Integer,Book> unique = new LinkedHashMap<>();

            for (Book book : ListOfSearchByText) {unique.put(book.getId(),book);}
            for (Book book : ListOfSearchByAuthor) {unique.put(book.getId(),book);}
            //Sort alphabetically
            List<Book> uniqueUnifiedeBooks = new ArrayList<>(unique.values());

            uniqueUnifiedeBooks.sort(
                            Comparator.comparing(
                                    Book::getTitle,
                                    String.CASE_INSENSITIVE_ORDER
                            )
                    );
            //Manual pagination
        int totalElements  = uniqueUnifiedeBooks.size();
        int start = (int) pageable.getOffset();
        int end   = Math.min(start + pageable.getPageSize(), totalElements );

        List<Book> pageContent;

        if (start >= totalElements ) {
            pageContent = List.of(); // empty
        } else {
            pageContent = uniqueUnifiedeBooks.subList(start, end);
        }

        return new PageImpl<>(pageContent, pageable, totalElements );


    }




}
