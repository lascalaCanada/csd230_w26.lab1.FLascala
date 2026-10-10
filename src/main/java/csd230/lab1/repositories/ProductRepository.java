package csd230.lab1.repositories;

import csd230.lab1.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for ProductEntity operations.
 * Allows generic persistence and retrieval operations across all product sub-types.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}