package nl.trifork.bookstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import nl.trifork.bookstore.data.entities.BookEntity;
import nl.trifork.bookstore.data.repositories.BookRepository;
import nl.trifork.bookstore.services.models.Book;
import nl.trifork.bookstore.services.models.BookSearchCriteria;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBook(Integer bookId) {
        return bookRepository.findById(bookId).map(BookEntity::convertToBook).orElse(null);
    }

    public Iterable<Book> getBooks(BookSearchCriteria criteria) {
        var books = new ArrayList<Book>();
        StreamSupport.stream(bookRepository.findAll().spliterator(), false).map(
                             BookEntity::convertToBook)
                     .forEach(books::add);
        if (criteria == null || criteria.isEmpty()) {
            return books;
        }

        return books
                .stream()
                .filter(book -> searchString(book.getTitle(), criteria.getTitle()) &&
                        searchString(book.getDescription(), criteria.getDescription()) &&
                        searchString(book.getAuthor(), criteria.getAuthor())
                        && priceEquals(book.getPrice(), criteria.getPrice()))
                .collect(Collectors.toList());
    }

    private boolean searchString(String searchable, String search) {
        return IsStringBlank(search) || searchable.toLowerCase(Locale.ROOT)
                                                  .contains(search.toLowerCase(Locale.ROOT));
    }

    private boolean priceEquals(BigDecimal bookPrice, BigDecimal criteriaPrice) {
        return criteriaPrice == null || criteriaPrice.compareTo(bookPrice) == 0;
    }

    private boolean IsStringBlank(String string) {
        return string == null || string.isBlank();
    }
}
