package csd230.lab1.entities;

import csd230.lab1.pojos.SaleableItem;
import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Abstract base class representing a generic Product entity for JPA persistence.
 * Implements Serializable and SaleableItem interfaces.
 */
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity implements Serializable, SaleableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;
    private String description;

    /**
     * Default no-argument constructor.
     */
    public ProductEntity() {
    }

    /**
     * Parameterized constructor for ProductEntity.
     *
     * @param price       The unit price of the product
     * @param description The textual description of the product
     */
    public ProductEntity(double price, String description) {
        this.price = price;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Abstract declaration of sellItem from SaleableItem interface.
     * Must be implemented by all concrete child entities.
     */
    @Override
    public abstract void sellItem();

    @Override
    public String toString() {
        return "ProductEntity{id=" + id + ", price=" + price + ", description='" + description + "'}";
    }
}