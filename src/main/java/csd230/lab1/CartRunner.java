package csd230.lab1;

import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.repositories.LaptopRepository;
import csd230.lab1.repositories.SmartPhoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CartRunner implements CommandLineRunner {

    private final LaptopRepository laptopRepository;
    private final SmartPhoneRepository smartPhoneRepository;

    public CartRunner(LaptopRepository laptopRepository, SmartPhoneRepository smartPhoneRepository) {
        this.laptopRepository = laptopRepository;
        this.smartPhoneRepository = smartPhoneRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear existing records
        laptopRepository.deleteAll();
        smartPhoneRepository.deleteAll();

        // Seed Laptops
        LaptopEntity laptop1 = new LaptopEntity(1299.99, "High performance gaming laptop", "Dell", 16, 512, "Intel i7");
        LaptopEntity laptop2 = new LaptopEntity(1899.99, "Ultralight productivity laptop", "Apple", 16, 512, "M2");

        laptopRepository.save(laptop1);
        laptopRepository.save(laptop2);

        // Seed Smartphones
        SmartPhoneEntity phone1 = new SmartPhoneEntity(999.99, "Flagship smartphone with OLED display", "Samsung", 6.7, 4500);
        SmartPhoneEntity phone2 = new SmartPhoneEntity(1099.99, "Advanced camera smartphone", "Apple", 6.1, 3200);

        smartPhoneRepository.save(phone1);
        smartPhoneRepository.save(phone2);

        System.out.println("Database successfully seeded with Laptops and Smartphones!");
    }
}