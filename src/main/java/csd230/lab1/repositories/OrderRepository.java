package csd230.lab1.repositories;

import csd230.lab1.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository interface for OrderEntity operations.
 * Provides standard CRUD and pagination functionality out of the box.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}