package nl.trifork.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import nl.trifork.bookstore.services.BookService;
import nl.trifork.bookstore.services.models.Book;
import nl.trifork.bookstore.services.models.BookSearchCriteria;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/books/{bookId}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Book> getBookDetails(
            @PathVariable Integer bookId,
            @RequestHeader("content-type") MediaType mediaType
                                              ) {
        return ResponseEntity.ok().contentType(mediaType).body(bookService.getBook(bookId));
    }

    @GetMapping(value = "/books", produces = {"application/json", "application/xml"})
    public ResponseEntity<Iterable<Book>> getBookList(
            @RequestHeader("content-type") MediaType mediaType,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) BigDecimal price
                                                     ) {
        return ResponseEntity.ok().contentType(mediaType).body(bookService.getBooks(
                new BookSearchCriteria(title, description, author, price)));
    }
}
