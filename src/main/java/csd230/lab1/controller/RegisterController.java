package csd230.lab1.controller;

import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    // Same BCrypt encoder defined in WebSecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Shows the empty registration form
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Receives the form data and creates the new user
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {

        // Remove accidental spaces before and after the name
        String cleanName = username.trim();

        // The form already has "required", but this protects against empty values too
        if (cleanName.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Username and password are required.");
            return "register";
        }

        // The username column is unique, so check first to show a friendly message
        if (userRepository.findByUsername(cleanName) != null) {
            model.addAttribute("error", "This username is already taken.");
            return "register";
        }

        // New accounts are always regular users, and the password is saved as a BCrypt hash
        UserEntity newUser = new UserEntity(cleanName, passwordEncoder.encode(password), "USER");
        userRepository.save(newUser);

        // Go to the login page, which shows a success message
        return "redirect:/login?registered";
    }
}