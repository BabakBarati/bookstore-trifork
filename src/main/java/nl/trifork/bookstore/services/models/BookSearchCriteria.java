package nl.trifork.bookstore.services.models;

import java.math.BigDecimal;

public class BookSearchCriteria {
    private final String title;
    private final String description;
    private final String author;
    private final BigDecimal price;

    public BookSearchCriteria(String title, String description, String author,
                              BigDecimal price) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isEmpty() {
        return checkEmptyString(title) && checkEmptyString(description) && checkEmptyString(author)
                && price == null;
    }

    private boolean checkEmptyString(String string) {
        return string == null || string.isBlank();
    }
}
