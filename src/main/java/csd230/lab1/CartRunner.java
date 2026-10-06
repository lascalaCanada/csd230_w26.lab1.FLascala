package csd230.lab1;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.repositories.CartRepository;
import csd230.lab1.repositories.LaptopRepository;
import csd230.lab1.repositories.SmartPhoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CommandLineRunner implementation to seed initial Products and Carts into the MySQL database.
 */
@Component
public class CartRunner implements CommandLineRunner {

    private final LaptopRepository laptopRepository;
    private final SmartPhoneRepository smartPhoneRepository;
    private final CartRepository cartRepository;

    public CartRunner(LaptopRepository laptopRepository,
                      SmartPhoneRepository smartPhoneRepository,
                      CartRepository cartRepository) {
        this.laptopRepository = laptopRepository;
        this.smartPhoneRepository = smartPhoneRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Clear existing records in correct order to respect foreign keys
        cartRepository.deleteAll();
        laptopRepository.deleteAll();
        smartPhoneRepository.deleteAll();

        // 1. Seed Laptops
        LaptopEntity laptop1 = new LaptopEntity(1299.99, "High performance gaming laptop", "Dell", 16, 512, "Intel i7");
        LaptopEntity laptop2 = new LaptopEntity(1899.99, "Ultralight productivity laptop", "Apple", 16, 512, "M2");

        laptop1 = laptopRepository.save(laptop1);
        laptop2 = laptopRepository.save(laptop2);

        // 2. Seed Smartphones
        SmartPhoneEntity phone1 = new SmartPhoneEntity(999.99, "Flagship smartphone with OLED display", "Samsung", 6.7, 4500);
        SmartPhoneEntity phone2 = new SmartPhoneEntity(1099.99, "Advanced camera smartphone", "Apple", 6.1, 3200);

        phone1 = smartPhoneRepository.save(phone1);
        phone2 = smartPhoneRepository.save(phone2);

        // 3. Seed Carts and link products (Fixes Professor Feedback #5)
        CartEntity cart1 = new CartEntity();
        cart1.addProduct(laptop1);
        cart1.addProduct(phone1);

        CartEntity cart2 = new CartEntity();
        cart2.addProduct(laptop2);
        cart2.addProduct(phone2);

        cartRepository.save(cart1);
        cartRepository.save(cart2);

        System.out.println("==================================================");
        System.out.println("Database successfully seeded with Products and Carts!");
        System.out.println("Cart 1 Saved: " + cart1);
        System.out.println("Cart 2 Saved: " + cart2);
        System.out.println("==================================================");
    }
}