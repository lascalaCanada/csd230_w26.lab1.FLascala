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

    private int ramGb;
    private int storageGb;
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
    public LaptopEntity(double price, String description, String brand, int ramGb, int storageGb, String processor, int copies) {
        super(price, description, brand, copies);
        this.ramGb = ramGb;
        this.storageGb = storageGb;
        this.processor = processor;
    }

    // Getters and Setters

    public int getRamGb() {
        return ramGb;
    }

    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    public int getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(int storageGb) {
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
        if (getCopies() > 0) {
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