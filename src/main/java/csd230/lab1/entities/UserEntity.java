package csd230.lab1.entities;

import jakarta.persistence.*;

// Represents a person who can log in to the application.
// The table is called "users" because "user" is a reserved word in some databases.
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Two people cannot share the same username, so the column is unique
    @Column(nullable = false, unique = true)
    private String username;

    // Never the plain text: this will always hold the BCrypt hash
    @Column(nullable = false)
    private String password;

    // Stored as plain text, for example "ADMIN" or "USER" (without the ROLE_ prefix)
    @Column(nullable = false)
    private String role;

    // JPA needs an empty constructor
    public UserEntity() {
    }

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}