package csd230.lab1.entities;

import jakarta.persistence.Entity;

@Entity
public abstract class PublicationEntity extends ProductEntity {
    private String title;
    //@Column(name = "pub_price") private double price;
    // Wrapper type (Integer) so a JSON null can be read without an error; the default is 0
    private Integer copies = 0;
    public PublicationEntity() {}
    public PublicationEntity(String t, Double p, Integer c) { this.title = t; setPrice(p); this.copies = c; }

    @Override public void sellItem() {
        // copies can be null, so it is checked before the comparison
        if (copies != null && copies > 0) { copies--; System.out.println("Sold '" + title + "'. Remaining copies: " + copies); }
        else { System.out.println("Cannot sell '" + title + "'. Out of stock."); }
    }
    //@Override public double getPrice() { return price; }

    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    //public void setPrice(double p) { this.price = p; }
    public Integer getCopies() { return copies; }
    public void setCopies(Integer c) { this.copies = c; }
    @Override public String toString() { return "Pub{title='" + title + "', price=" + getPrice() + ", copies=" + copies + "}"; }
}
