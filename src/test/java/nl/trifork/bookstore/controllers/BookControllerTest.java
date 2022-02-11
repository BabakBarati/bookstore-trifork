package nl.trifork.bookstore.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import nl.trifork.bookstore.TestConfiguration;
import nl.trifork.bookstore.data.entities.AuthorEntity;
import nl.trifork.bookstore.data.entities.BookEntity;
import nl.trifork.bookstore.data.repositories.AuthorRepository;
import nl.trifork.bookstore.data.repositories.BookRepository;
import nl.trifork.bookstore.services.BookService;
import nl.trifork.bookstore.services.models.Book;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(BookController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfiguration.class)
class BookControllerTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private BookService bookService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void getBookDetails_when_xml_expect_xml() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var response = restTemplate.exchange("http://localhost:" + port + "/books/1", HttpMethod.GET,
                                             new HttpEntity<>(headers), Book.class);

        // assert
        var responseContentType = response.getHeaders().get("content-type").get(0);
        Assertions.assertEquals("application/xml", responseContentType);
    }

    @Test
    void getBookDetails_when_json_json_xml() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var response = restTemplate.exchange("http://localhost:" + port + "/books/1", HttpMethod.GET,
                                             new HttpEntity<>(headers), Book.class);

        // assert
        var responseContentType = response.getHeaders().get("content-type").get(0);
        Assertions.assertEquals("application/xml", responseContentType);
    }

    @Test
    void getBookDetails_when_bookExists_expect_returnBook() {
        // arrange
        var authorEntity = authorRepository.save(new AuthorEntity("Babak"));
        var bookEntity = bookRepository.save(
                new BookEntity("Book Title", "Some description", "/imgs/cover.jpg",
                               new BigDecimal("12.2"), authorEntity));

        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var book =
                restTemplate.exchange("http://localhost:" + port + "/books/" + bookEntity.getId(),
                                      HttpMethod.GET,
                                      new HttpEntity<>(headers), Book.class).getBody();

        // assert
        Assertions.assertNotNull(book);
        Assertions.assertEquals(bookEntity.getId(), book.getId());
        Assertions.assertEquals(bookEntity.getTitle(), book.getTitle());
        Assertions.assertEquals(bookEntity.getDescription(), book.getDescription());
        Assertions.assertEquals(bookEntity.getAuthor().getPseudonym(), book.getAuthor());
        Assertions.assertEquals(bookEntity.getCover(), book.getCover());
        Assertions.assertEquals(0, BigDecimal.valueOf(12.2).compareTo(book.getPrice()));
    }

    @Test
    void getBookDetails_when_bookNotExists_expect_returnNull() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var book =
                restTemplate.exchange("http://localhost:" + port + "/books/" + new Random().nextInt(),
                                      HttpMethod.GET,
                                      new HttpEntity<>(headers), Book.class).getBody();

        // assert
        Assertions.assertNull(book);
    }

    @Test
    void getBookList_when_xml_expect_xml() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var response = restTemplate.exchange("http://localhost:" + port + "/books/", HttpMethod.GET,
                                             new HttpEntity<>(headers), Book[].class);

        // assert
        var responseContentType = response.getHeaders().get("content-type").get(0);
        Assertions.assertEquals("application/xml", responseContentType);
    }

    @Test
    void getBookList_when_json_json_xml() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // act
        var response = restTemplate.exchange("http://localhost:" + port + "/books/", HttpMethod.GET,
                                             new HttpEntity<>(headers), Book[].class);

        // assert
        var responseContentType = response.getHeaders().get("content-type").get(0);
        Assertions.assertEquals("application/json", responseContentType);
    }

    @Test
    void getBookList_when_noBookExists_expect_emptyArray() {
        // arrange
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var books = restTemplate.exchange("http://localhost:" + port + "/books/", HttpMethod.GET,
                                          new HttpEntity<>(headers), Book[].class).getBody();

        // assert
        Assertions.assertEquals(0, books.length);
    }

    @Test
    void getBookList_when_booksExist_expect_arrayOfBooks() {
        // arrange
        seedMultipleBooks();
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        // act
        var books = restTemplate.exchange("http://localhost:" + port + "/books/", HttpMethod.GET,
                                          new HttpEntity<>(headers), Book[].class).getBody();

        // assert
        Assertions.assertEquals(3, books.length);
        var firstBook = books[0];
        Assertions.assertNotNull(firstBook);
        Assertions.assertNotNull(firstBook.getId());
        Assertions.assertNotNull(firstBook.getTitle());
        Assertions.assertNotNull(firstBook.getDescription());
        Assertions.assertNotNull(firstBook.getAuthor());
        Assertions.assertNotNull(firstBook.getPrice());
    }

    @Test
    void getBookList_when_criteriaMatches_expect_returnRelatedBooks() {
        // arrange
        seedMultipleBooks();
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // act
        var url = "http://localhost:" + port + "/books/?author=Babak&description=some&&price=12.2";
        var books = restTemplate.exchange(url, HttpMethod.GET,
                                          new HttpEntity<>(headers), Book[].class).getBody();

        // assert
        Assertions.assertEquals(1, books.length);
        Assertions.assertEquals("Book First", books[0].getTitle());
    }

    private void seedMultipleBooks() {
        var author1 = authorRepository.save(new AuthorEntity("Babak"));
        var author2 = authorRepository.save(new AuthorEntity("Joris"));
        var bookEntity1 =
                new BookEntity("Book First", "Some description", "/imgs/cover1.jpg",
                               new BigDecimal("12.2"), author1);
        var bookEntity2 =
                new BookEntity("Book Second", "Some description 2", "/imgs/cover2.jpg",
                               new BigDecimal("14.5"), author1);
        var bookEntity3 =
                new BookEntity("Book Thirs", "notes about the book here", "/imgs/cover3.jpg",
                               new BigDecimal(7), author2);
        bookRepository.saveAll(Arrays.asList(bookEntity1, bookEntity2, bookEntity3));
    }
}