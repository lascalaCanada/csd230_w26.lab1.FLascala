package csd230.lab1.repositories;

import csd230.lab1.entities.SmartPhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for SmartPhoneEntity with custom derived queries.
 */
@Repository
public interface SmartPhoneRepository extends JpaRepository<SmartPhoneEntity, Long> {

    // Derived Query 1: Find smartphones by brand
    List<SmartPhoneEntity> findByBrand(String brand);

    // Derived Query 2: Find smartphones with screen size greater than specified inches
    List<SmartPhoneEntity> findByScreenSizeGreaterThan(double screenSize);
}