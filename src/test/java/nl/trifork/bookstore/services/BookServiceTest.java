package nl.trifork.bookstore.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import nl.trifork.bookstore.TestConfiguration;
import nl.trifork.bookstore.data.entities.AuthorEntity;
import nl.trifork.bookstore.data.entities.BookEntity;
import nl.trifork.bookstore.data.repositories.AuthorRepository;
import nl.trifork.bookstore.data.repositories.BookRepository;
import nl.trifork.bookstore.services.models.BookSearchCriteria;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = TestConfiguration.class)
class BookServiceTest {
    @Autowired private BookService bookService;
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void getBook_when_bookExists_expect_returnBook() {
        // arrange
        var authorEntity = authorRepository.save(new AuthorEntity("Babak"));
        var bookEntity = bookRepository.save(
                new BookEntity("Book Title", "Some description", "/imgs/cover.jpg",
                               new BigDecimal("12.2"), authorEntity));

        // act
        var book = bookService.getBook(bookEntity.getId());

        // assert
        Assertions.assertEquals(bookEntity.getId(), book.getId());
        Assertions.assertEquals(bookEntity.getTitle(), book.getTitle());
        Assertions.assertEquals(bookEntity.getDescription(), book.getDescription());
        Assertions.assertEquals(bookEntity.getAuthor().getPseudonym(), book.getAuthor());
        Assertions.assertEquals(bookEntity.getCover(), book.getCover());
        Assertions.assertEquals(0, BigDecimal.valueOf(12.2).compareTo(book.getPrice()));
    }

    @Test
    public void getBook_when_bookNotExists_expect_returnNull() {
        // arrange
        var book = bookService.getBook(1);

        // assert
        Assertions.assertNull(book);
    }

    @Test
    public void getBooks_when_noBookExists_expect_returnEmpty() {
        // arrange
        // nothing

        // acts
        var books = bookService.getBooks(null);
        Assertions.assertFalse(books.iterator().hasNext());
    }

    @Test
    public void getBooks_when_noCriteria_expect_returnAllBooks() {
        // arrange
        seedMultipleBooks();

        // act
        var books = bookService.getBooks(null);

        // assert
        Assertions.assertEquals(3, StreamSupport.stream(books.spliterator(), false).count());
        var firstBook = books.iterator().next();
        Assertions.assertNotNull(firstBook);
        Assertions.assertNotNull(firstBook.getId());
        Assertions.assertNotNull(firstBook.getTitle());
        Assertions.assertNotNull(firstBook.getDescription());
        Assertions.assertNotNull(firstBook.getAuthor());
        Assertions.assertNotNull(firstBook.getPrice());
    }

    @Test
    public void getBooks_when_criteriaMatches_expect_returnRelatedBooks() {
        // arrange
        seedMultipleBooks();

        // act
        var booksByAuthor = bookService.getBooks(new BookSearchCriteria(null, null, "Babak", null));

        // assert
        int counter = 0;
        var iterator = booksByAuthor.iterator();
        while (iterator.hasNext()) {
            var book = iterator.next();
            Assertions.assertEquals("Babak", book.getAuthor());
            counter++;
        }
        Assertions.assertEquals(2, counter);

        var bookByTitle = bookService.getBooks(new BookSearchCriteria("first", null, null, null));
        iterator = bookByTitle.iterator();
        Assertions.assertEquals("Book First", iterator.next().getTitle());
        Assertions.assertFalse(iterator.hasNext());

        var bookByDescription =
                bookService.getBooks(new BookSearchCriteria(null, "first", null, null));
        iterator = bookByDescription.iterator();
        Assertions.assertEquals("Book First", iterator.next().getTitle());
        Assertions.assertFalse(iterator.hasNext());

        var bookByPrice = bookService.getBooks(
                new BookSearchCriteria(null, null, null, new BigDecimal("14.5")));
        iterator = bookByPrice.iterator();
        Assertions.assertEquals("Book Second", iterator.next().getTitle());
        Assertions.assertFalse(iterator.hasNext());
    }

    private void seedMultipleBooks() {
        var author1 = authorRepository.save(new AuthorEntity("Babak"));
        var author2 = authorRepository.save(new AuthorEntity("Joris"));
        var bookEntity1 = new BookEntity("Book First", "Some description first", "/imgs/cover1.jpg",
                                         new BigDecimal("12.2"), author1);
        var bookEntity2 = new BookEntity("Book Second", "Some description 2", "/imgs/cover2.jpg",
                                         new BigDecimal("14.5"), author1);
        var bookEntity3 = new BookEntity("Book Third", "Some description 3", "/imgs/cover3.jpg",
                                         new BigDecimal(7), author2);
        bookRepository.saveAll(Arrays.asList(bookEntity1, bookEntity2, bookEntity3));
    }
}