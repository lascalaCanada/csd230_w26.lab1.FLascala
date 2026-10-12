package csd230.lab1.config;

import csd230.lab1.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Central place for the security rules of the application
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Passwords are saved as BCrypt hashes, never as plain text.
    // Other classes (register page, CartRunner) inject this same encoder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Connects the login process with our database users and the BCrypt encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Defines which URLs are open, which need a login and which need the ADMIN role.
    // Rules are checked from top to bottom, so the specific ones come before anyRequest().
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        // Anyone can see the login page, the register page and static files.
                        // /error is open too so real error messages are not hidden behind the login.
                        .requestMatchers("/login", "/register", "/error", "/css/**", "/js/**").permitAll()
                        // Only admins can add, edit or delete books, laptops and smartphones
                        .requestMatchers("/books/add", "/books/edit/**", "/books/delete/**",
                                "/laptops/add", "/laptops/edit/**", "/laptops/delete/**",
                                "/smartphones/add", "/smartphones/edit/**", "/smartphones/delete/**").hasRole("ADMIN")
                        // Everything else just needs a logged-in user
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Our own login page instead of the default one from Spring
                        .loginPage("/login")
                        // After a successful login, always go to the book list
                        .defaultSuccessUrl("/books", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        // After logging out, go back to the login page with a small message
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}