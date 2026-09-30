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
     */
    public SmartPhoneEntity(double price, String description, String brand, double screenSize, int batteryCapacity) {
        super(price, description, brand);
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
     */
    @Override
    public void sellItem() {
        System.out.println("Selling SmartPhone Entity: " + getBrand() + " - " + getDescription() + " for $" + getPrice());
    }

    @Override
    public String toString() {
        return "SmartPhoneEntity{" +
                "screenSize=" + screenSize +
                ", batteryCapacity=" + batteryCapacity +
                "} " + super.toString();
    }
}