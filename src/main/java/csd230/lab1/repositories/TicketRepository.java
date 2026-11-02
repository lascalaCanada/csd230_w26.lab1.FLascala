package csd230.lab1.repositories;

import csd230.lab1.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for TicketEntity operations (used by the REST controllers).
 */
@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
}
