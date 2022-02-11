package nl.trifork.bookstore.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import nl.trifork.bookstore.data.entities.BookEntity;
import nl.trifork.bookstore.data.repositories.BookRepository;

public class TestBookRepository implements BookRepository {
    private Map<Integer, BookEntity> books = new HashMap<>();

    @Override
    public <S extends BookEntity> S save(S entity) {
        books.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends BookEntity> Iterable<S> saveAll(Iterable<S> entities) {
        for (S book : entities) {
            books.put(book.getId(), book);
        }
        return entities;
    }

    @Override
    public Optional<BookEntity> findById(Integer id) {
        var entity = books.get(id);
        return entity == null ? Optional.empty() : Optional.of(entity);
    }

    @Override
    public boolean existsById(Integer id) {
        return books.containsKey(id);
    }

    @Override
    public Iterable<BookEntity> findAll() {
        return books.values();
    }

    @Override
    public Iterable<BookEntity> findAllById(Iterable<Integer> ids) {
        var idCollection = StreamSupport.stream(ids.spliterator(), false)
                                        .collect(Collectors.toList());
        return books.values().stream().filter(book -> idCollection.contains(book.getId()))
                    .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return books.size();
    }

    @Override
    public void deleteById(Integer id) {
        books.remove(id);
    }

    @Override
    public void delete(BookEntity entity) {
        books.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        ids.forEach(books.keySet()::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends BookEntity> entities) {
        var idCollection = new ArrayList<Integer>();
        StreamSupport.stream(entities.spliterator(), false).map(BookEntity::getId)
                     .forEach(idCollection::add);
        idCollection.forEach(books.keySet()::remove);
    }

    @Override
    public void deleteAll() {
        books = new HashMap<>();
    }
}
