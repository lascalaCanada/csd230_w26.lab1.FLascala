package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Concrete entity class representing a SmartPhone product for JPA persistence.
 * Extends ElectronicProductEntity and implements sellItem logic.
 */
@Entity
@DiscriminatorValue("SMARTPHONE")
public class SmartPhoneEntity extends ElectronicProductEntity {

    private double screenSize;
    private int batteryCapacity;
    // No 'copies' field here: the stock counter lives in ElectronicProductEntity.
    // Declaring it again would map the same column twice.

    /**
     * Default no-argument constructor.
     */
    public SmartPhoneEntity() {
        super();
    }

    /**
     * Parameterized constructor for SmartPhoneEntity.
     *
     * @param price           The unit price
     * @param description     The description
     * @param brand           The brand name
     * @param screenSize      Screen size in inches
     * @param batteryCapacity Battery capacity in mAh
     * @param copies          Initial stock quantity
     */
    public SmartPhoneEntity(double price, String description, String brand, double screenSize, int batteryCapacity, int copies) {
        super(price, description, brand, copies);
        this.screenSize = screenSize;
        this.batteryCapacity = batteryCapacity;
    }

    // Getters and Setters

    public double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Concrete implementation of sellItem from SaleableItem interface.
     * Lowers the stock by one unit, as long as there is stock left.
     */
    @Override
    public void sellItem() {
        if (getCopies() > 0) {
            setCopies(getCopies() - 1);
            System.out.println("Selling SmartPhone Entity: " + getBrand() + " - " + getDescription()
                    + " for $" + getPrice() + ". Remaining copies: " + getCopies());
        } else {
            System.out.println("Cannot sell SmartPhone Entity: " + getBrand() + " - " + getDescription() + ". Out of stock.");
        }
    }

    @Override
    public String toString() {
        return "SmartPhoneEntity{" +
                "screenSize=" + screenSize +
                ", batteryCapacity=" + batteryCapacity +
                "} " + super.toString();
    }
}