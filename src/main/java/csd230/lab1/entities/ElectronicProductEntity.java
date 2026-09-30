package csd230.lab1.entities;

import jakarta.persistence.Entity;

/**
 * Abstract entity representing the ElectronicProduct category in the database.
 * Inherits from ProductEntity.
 */
@Entity
public abstract class ElectronicProductEntity extends ProductEntity {

    private String brand;

    /**
     * Default constructor required by JPA.
     */
    public ElectronicProductEntity() {
        super();
    }

    /**
     * Constructor accepting brand.
     *
     * @param brand Brand name
     */
    public ElectronicProductEntity(String brand) {
        super();
        this.brand = brand;
    }

    /**
     * Parameterized constructor.
     *
     * @param price       Product price
     * @param description Product description
     * @param brand       Brand name
     */
    public ElectronicProductEntity(double price, String description, String brand) {
        super(price, description);
        this.brand = brand;
    }

    // Getters and Setters

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "ElectronicProductEntity{" +
                "brand='" + brand + '\'' +
                "} " + super.toString();
    }
}