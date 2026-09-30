package csd230.lab1.repositories;

import csd230.lab1.entities.SmartPhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartPhoneRepository extends JpaRepository<SmartPhoneEntity, Long> {
}