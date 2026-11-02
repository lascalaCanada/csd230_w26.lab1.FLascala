package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity @DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {
    public TicketEntity() {}
    // price and description are inherited from ProductEntity, so they are saved in the same columns as the other products
    public TicketEntity(String d, Double p) {
        setDescription(d);
        setPrice(p);
    }

    @Override public void sellItem() { System.out.println("Selling Ticket: " + getDescription() + " for $" + getPrice()); }

    @Override public String toString() { return "Ticket{desc='" + getDescription() + "', price=" + getPrice() + "}"; }
}
