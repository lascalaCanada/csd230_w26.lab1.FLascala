package csd230.lab1.services;

import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Tells Spring Security where to look for users: in our own "users" table
// instead of the in-memory default user.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Spring Security calls this method every time someone submits the login form
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Look for the user in the database
        UserEntity user = userRepository.findByUsername(username);

        // findByUsername returns null when nobody has that name
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Convert our UserEntity into the object Spring Security understands.
        // The password here is the BCrypt hash saved in the database; Spring compares it
        // with what the person typed. roles("ADMIN") becomes the authority ROLE_ADMIN,
        // which is what hasRole("ADMIN") checks later.
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}