package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private List<Book> books = new ArrayList<>();

    @Override
    public Map<String, Long> getBooks() {
        return books.stream()
                .flatMap(book -> book.getGenres().stream()
                        .map(genre -> new AbstractMap.SimpleEntry<>(genre, 1L)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return books.stream()
                .filter(book ->
                        (searchCriteria.getTitle() == null || searchCriteria.getTitle().isEmpty() || book.getTitle().toLowerCase().contains(searchCriteria.getTitle().toLowerCase())) &&
                                (searchCriteria.getAuthor() == null || searchCriteria.getAuthor().isEmpty() || book.getAuthor().toLowerCase().contains(searchCriteria.getAuthor().toLowerCase())) &&
                                (searchCriteria.getGenre() == null || searchCriteria.getGenre().isEmpty() || book.getGenres().stream().anyMatch(genre -> genre.equalsIgnoreCase(searchCriteria.getGenre())))
                )
                .sorted(Comparator.comparing(Book::getPublicationDate))
                .collect(Collectors.toList());
    }
}
