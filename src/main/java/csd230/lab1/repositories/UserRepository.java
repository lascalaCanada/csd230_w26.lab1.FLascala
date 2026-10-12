package csd230.lab1.repositories;

import csd230.lab1.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Spring builds this query from the method name: select * from users where username = ?
    // Used by the login and by the cart controller to find the logged-in user
    UserEntity findByUsername(String username);
}