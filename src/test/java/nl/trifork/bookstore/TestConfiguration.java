package nl.trifork.bookstore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import nl.trifork.bookstore.data.repositories.AuthorRepository;
import nl.trifork.bookstore.data.repositories.BookRepository;
import nl.trifork.bookstore.services.BookService;

@ComponentScan(basePackageClasses = {
        BookService.class,
        BookRepository.class,
        AuthorRepository.class})
@SpringBootApplication
public class TestConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(TestConfiguration.class, args);
    }
}
