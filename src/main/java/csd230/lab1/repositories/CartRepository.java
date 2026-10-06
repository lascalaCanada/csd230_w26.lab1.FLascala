package csd230.lab1.repositories;

import csd230.lab1.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository interface for managing CartEntity persistence operations.
 */
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
}