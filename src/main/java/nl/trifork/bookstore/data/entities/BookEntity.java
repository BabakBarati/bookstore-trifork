package nl.trifork.bookstore.data.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.trifork.bookstore.services.models.Book;

@Entity
@Table(name = "Books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "cover")
    private String cover;
    @Column(name = "price")
    private BigDecimal price;

    public BookEntity() {
    }

    public BookEntity(String title, String description, String cover,
                      BigDecimal price, AuthorEntity author) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.price = price;
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Book convertToBook() {
        return new Book(id, title, description, author.getPseudonym(), cover, price);
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }
}
