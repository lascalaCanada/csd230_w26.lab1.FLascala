package csd230.lab1.repositories;

import csd230.lab1.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import csd230.lab1.entities.UserEntity;

/**
 * Spring Data JPA repository for CartEntity operations.
 */
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    // Spring builds the query from the name: select * from cart_entity where user_id = ?
    // Returns null when the user doesn't have a cart yet
    CartEntity findByUser(UserEntity user);
}