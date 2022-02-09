package nl.trifork.bookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import nl.trifork.bookstore.models.BookResponse;
import nl.trifork.bookstore.services.BookService;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/books/{bookId}", produces = {"application/json", "application/xml"})
    public BookResponse getBookDetails(@PathVariable String bookId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping(value = "/books", produces = {"application/json", "application/xml"})
    public List<BookResponse> getBookList() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
