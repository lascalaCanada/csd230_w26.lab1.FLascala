package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete entity class representing a Laptop product for JPA persistence.
 * Extends ElectronicProductEntity and implements sellItem logic.
 */
@Entity
@DiscriminatorValue("LAPTOP")
public class LaptopEntity extends ElectronicProductEntity {

    // Wrapper types (Integer) so a JSON null can be read without an error; the default is 0
    private Integer ramGb = 0;
    private Integer storageGb = 0;
    private String processor;
    // No 'copies' field here: the stock counter lives in ElectronicProductEntity.
    // Declaring it again would map the same column twice.

    /**
     * Default no-argument constructor.
     */
    public LaptopEntity() {
        super();
    }

    /**
     * Parameterized constructor for LaptopEntity.
     *
     * @param price       The unit price
     * @param description The description
     * @param brand       The brand name
     * @param ramGb       RAM size in GB
     * @param storageGb   Storage capacity in GB
     * @param processor   Processor model
     * @param copies      Initial stock quantity
     */
    public LaptopEntity(Double price, String description, String brand, Integer ramGb, Integer storageGb, String processor, Integer copies) {
        super(price, description, brand, copies);
        this.ramGb = ramGb;
        this.storageGb = storageGb;
        this.processor = processor;
    }

    // Getters and Setters

    public Integer getRamGb() {
        return ramGb;
    }

    public void setRamGb(Integer ramGb) {
        this.ramGb = ramGb;
    }

    public Integer getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(Integer storageGb) {
        this.storageGb = storageGb;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * Concrete implementation of sellItem from SaleableItem interface.
     * Lowers the stock by one unit, as long as there is stock left.
     */
    @Override
    public void sellItem() {
        // copies can be null, so it is checked before the comparison
        if (getCopies() != null && getCopies() > 0) {
            setCopies(getCopies() - 1);
            System.out.println("Selling Laptop Entity: " + getBrand() + " - " + getDescription()
                    + " for $" + getPrice() + ". Remaining copies: " + getCopies());
        } else {
            System.out.println("Cannot sell Laptop Entity: " + getBrand() + " - " + getDescription() + ". Out of stock.");
        }
    }

    @Override
    public String toString() {
        return "LaptopEntity{" +
                "ramGb=" + ramGb +
                ", storageGb=" + storageGb +
                ", processor='" + processor + '\'' +
                "} " + super.toString();
    }
}