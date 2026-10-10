package csd230.lab1.entities;

import jakarta.persistence.Entity;

/**
 * Abstract entity representing the ElectronicProduct category in the database.
 * Inherits from ProductEntity.
 */
@Entity
public abstract class ElectronicProductEntity extends ProductEntity {

    private String brand;
    private int copies;

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
     * @param copies      Stock volume
     */
    public ElectronicProductEntity(double price, String description, String brand, int copies) {
        super(price, description);
        this.brand = brand;
        this.copies = copies;
    }

    // Getters and Setters

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCopies() { return copies; }

    public void setCopies(int copies) { this.copies = copies; }

    @Override
    public String toString() {
        return "ElectronicProductEntity{" +
                "brand='" + brand + '\'' +
                "} " + super.toString();
    }
}