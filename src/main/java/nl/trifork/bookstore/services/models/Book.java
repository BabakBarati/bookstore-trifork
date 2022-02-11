package nl.trifork.bookstore.services.models;

import java.math.BigDecimal;

public class Book {
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String cover;
    private BigDecimal price;

    public Book() {
    }

    public Book(Integer id, String title, String description, String author, String cover,
                BigDecimal price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.cover = cover;
        this.price = price;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
