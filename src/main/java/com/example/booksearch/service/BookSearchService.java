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


    private static final int MIN_QUERY_LENGTH = 2;
    private static final int FUZZY_MIN_LENGTH = 3;
    private static final int MAX_DISTANCE = 2;


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
    public List<String> suggest(String q,int limit){

        if (q == null || q.trim().length() < MIN_QUERY_LENGTH) {
            return List.of();
        }
        q = q.trim();
        // Substring suggestions (%q%)
        List<String> exact =
                bookRepository.suggestTitles(q, limit);

        if (exact.size() >= limit || q.length() < FUZZY_MIN_LENGTH) {
            return exact;
        }

        // Fuzzy fallback
        List<String> fuzzy =
                findFuzzyTitles(q, exact, limit);

        // Merge, dedupe, cap
        LinkedHashSet<String> merged = new LinkedHashSet<>();
        merged.addAll(exact);
        merged.addAll(fuzzy);

        return merged.stream()
                .limit(limit)
                .toList();

    }

        private List<String> findFuzzyTitles(
                String q,
                List<String> exact,
                int limit
        ) {
            char firstChar = q.charAt(0);

            List<String> candidates =
                    bookRepository.findTitlesByPrefix(
                            String.valueOf(firstChar)
                    );

            return candidates.stream()
                    .filter(title ->
                            !exact.contains(title) &&
                            EditDistanceUtil.levenshtein(q, title) <= MAX_DISTANCE
                    )
                    .sorted(
                            Comparator.comparingInt(
                                    title -> EditDistanceUtil.levenshtein(q, title)
                            )
                    )
                    .limit(limit - exact.size())
                    .toList();
        }





}
