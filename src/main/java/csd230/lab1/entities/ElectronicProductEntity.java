package csd230.lab1.entities;

import jakarta.persistence.Entity;

/**
 * Abstract entity representing the ElectronicProduct category in the database.
 * Inherits from ProductEntity.
 */
@Entity
public abstract class ElectronicProductEntity extends ProductEntity {

    private String brand;
    // Wrapper type (Integer) so a JSON null can be read without an error; the default is 0
    private Integer copies = 0;

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
    public ElectronicProductEntity(Double price, String description, String brand, Integer copies) {
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

    public Integer getCopies() { return copies; }

    public void setCopies(Integer copies) { this.copies = copies; }

    @Override
    public String toString() {
        return "ElectronicProductEntity{" +
                "brand='" + brand + '\'' +
                "} " + super.toString();
    }
}