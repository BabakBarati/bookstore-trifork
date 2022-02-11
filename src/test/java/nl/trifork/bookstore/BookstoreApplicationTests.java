package nl.trifork.bookstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nl.trifork.bookstore.controllers.BookController;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BookstoreApplication.class)
class BookstoreApplicationTests {
    @Autowired
    private BookController bookController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(bookController);
    }

}
