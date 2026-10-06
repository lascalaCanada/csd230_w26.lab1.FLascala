package csd230.lab1.repositories;

import csd230.lab1.entities.LaptopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for LaptopEntity with custom derived queries.
 */
@Repository
public interface LaptopRepository extends JpaRepository<LaptopEntity, Long> {

    // Derived Query 1: Find laptops by brand name (case sensitive or exact match)
    List<LaptopEntity> findByBrand(String brand);

    // Derived Query 2: Find laptops with RAM greater than or equal to specified GB
    List<LaptopEntity> findByRamGbGreaterThanEqual(int ramGb);

    // Derived Query 3: Find laptops priced lower than a specific amount
    List<LaptopEntity> findByPriceLessThan(double price);
}