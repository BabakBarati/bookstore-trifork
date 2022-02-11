package nl.trifork.bookstore.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.trifork.bookstore.data.entities.BookEntity;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Integer> {
}
