package csd230.lab1.repositories;

import csd230.lab1.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for BookEntity operations.
 */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}