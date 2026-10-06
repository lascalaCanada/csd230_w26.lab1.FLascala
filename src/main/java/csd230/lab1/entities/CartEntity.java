package csd230.lab1.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a shopping Cart in the system.
 * Holds a collection of ProductEntity items with JPA relationship mapping.
 */
@Entity
@Table(name = "carts")
public class CartEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products = new ArrayList<>();

    /**
     * Default no-argument constructor required by JPA.
     */
    public CartEntity() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    /**
     * Helper method to add a product to the cart.
     *
     * @param product ProductEntity to be added
     */
    public void addProduct(ProductEntity product) {
        if (product != null) {
            this.products.add(product);
        }
    }

    /**
     * Calculates the total price of all items currently in the cart.
     *
     * @return double representing the total cart value
     */
    public double getTotalPrice() {
        return products.stream()
                .mapToDouble(ProductEntity::getPrice)
                .sum();
    }

    @Override
    public String toString() {
        return "CartEntity{" +
                "id=" + id +
                ", productCount=" + products.size() +
                ", totalPrice=$" + getTotalPrice() +
                '}';
    }
}