package csd230.lab1.repositories;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.entities.SmartPhoneEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTests {

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private SmartPhoneRepository smartPhoneRepository;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
        laptopRepository.deleteAll();
        smartPhoneRepository.deleteAll();
    }

    @Test
    @DisplayName("Test LaptopRepository derived queries")
    void testLaptopDerivedQueries() {
        LaptopEntity laptop1 = new LaptopEntity(1299.99, "Gaming Laptop", "Dell", 16, 512, "Intel i7");
        LaptopEntity laptop2 = new LaptopEntity(1899.99, "MacBook Pro", "Apple", 32, 1000, "M3 Pro");

        laptopRepository.save(laptop1);
        laptopRepository.save(laptop2);

        List<LaptopEntity> appleLaptops = laptopRepository.findByBrand("Apple");
        assertEquals(1, appleLaptops.size());
        assertEquals("M3 Pro", appleLaptops.get(0).getProcessor());

        List<LaptopEntity> highRamLaptops = laptopRepository.findByRamGbGreaterThanEqual(16);
        assertEquals(2, highRamLaptops.size());

        List<LaptopEntity> budgetLaptops = laptopRepository.findByPriceLessThan(1500.00);
        assertEquals(1, budgetLaptops.size());
        assertEquals("Dell", budgetLaptops.get(0).getBrand());
    }

    @Test
    @DisplayName("Test Cart persistence with products")
    void testCartPersistence() {
        LaptopEntity laptop = laptopRepository.save(new LaptopEntity(1299.99, "Workstation", "HP", 16, 512, "i5"));
        SmartPhoneEntity phone = smartPhoneRepository.save(new SmartPhoneEntity(999.99, "Flagship Phone", "Samsung", 6.7, 4500));

        CartEntity cart = new CartEntity();
        cart.addProduct(laptop);
        cart.addProduct(phone);

        CartEntity savedCart = cartRepository.save(cart);

        assertNotNull(savedCart.getId());
        assertEquals(2, savedCart.getProducts().size());
        assertEquals(2299.98, savedCart.getTotalPrice(), 0.01);
    }
}