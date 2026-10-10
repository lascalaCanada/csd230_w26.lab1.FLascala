package csd230.lab1;

import csd230.lab1.entities.BookEntity;
import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.repositories.BookRepository;
import csd230.lab1.repositories.LaptopRepository;
import csd230.lab1.repositories.SmartPhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runner class to seed the database with initial sample products upon startup.
 */
@Component
public class CartRunner implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private SmartPhoneRepository smartPhoneRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed Books if table is empty using the parameterized constructor
        if (bookRepository.count() == 0) {
            BookEntity book1 = new BookEntity("Spring Boot in Action", 45.99, 10, "Craig Walls");
            bookRepository.save(book1);

            BookEntity book2 = new BookEntity("Effective Java", 52.50, 5, "Joshua Bloch");
            bookRepository.save(book2);
        }

        // Seed Laptops if table is empty
        if (laptopRepository.count() == 0) {
            LaptopEntity laptop1 = new LaptopEntity();
            laptop1.setDescription("Dell XPS 13 - 16GB RAM, 512GB SSD");
            laptop1.setPrice(1299.99);
            laptop1.setCopies(7);
            laptop1.setStorageGb(512);
            laptop1.setRamGb(16);
            laptop1.setBrand("Dell");
            laptop1.setProcessor("Power Advanced");
            laptopRepository.save(laptop1);

            LaptopEntity laptop2 = new LaptopEntity();
            laptop2.setDescription("MacBook Air M2 - 8GB RAM, 256GB SSD");
            laptop2.setPrice(1099.00);
            laptop2.setCopies(5);
            laptop2.setStorageGb(256);
            laptop2.setRamGb(8);
            laptop2.setBrand("Apple");
            laptop2.setProcessor("Apple Processor");
            laptopRepository.save(laptop2);
        }

        // Seed Smartphones if table is empty
        if (smartPhoneRepository.count() == 0) {
            SmartPhoneEntity phone1 = new SmartPhoneEntity();
            phone1.setDescription("iPhone 15 Pro - 128GB");
            phone1.setPrice(999.99);
            phone1.setCopies(8);
            smartPhoneRepository.save(phone1);

            SmartPhoneEntity phone2 = new SmartPhoneEntity();
            phone2.setDescription("Samsung Galaxy S24 - 256GB");
            phone2.setPrice(899.99);
            phone2.setCopies(9);
            smartPhoneRepository.save(phone2);
        }
    }
}