package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a Book, extending PublicationEntity.
 */
@Entity
@DiscriminatorValue("BOOK")
public class BookEntity extends PublicationEntity {

    private String author;

    /**
     * Default no-args constructor required by JPA.
     */
    public BookEntity() {
    }

    /**
     * Parametrized constructor.
     *
     * @param title  Book title
     * @param price  Book unit price
     * @param copies Initial stock quantity
     * @param author Book author name
     */
    public BookEntity(String title, double price, int copies, String author) {
        super(title, price, copies);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{author='" + author + "', " + super.toString() + "}";
    }
}