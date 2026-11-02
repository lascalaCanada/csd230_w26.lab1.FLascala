package csd230.lab1.repositories;

import csd230.lab1.entities.MagazineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for MagazineEntity operations (used by the REST controllers).
 */
@Repository
public interface MagazineRepository extends JpaRepository<MagazineEntity, Long> {
}
