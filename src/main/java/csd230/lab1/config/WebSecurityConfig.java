package csd230.lab1.config;

import csd230.lab1.auth.JwtAuthorizationFilter;
import csd230.lab1.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

// Central place for the security rules of the application
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Filter that reads the JWT from the Authorization header (see JwtAuthorizationFilter)
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

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

    // The login endpoint (AuthController) uses this manager to check username and password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
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
                        // The Swagger pages are open. The REST API is not open anymore: it needs a JWT
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // The login endpoint must be open, otherwise nobody could get a token
                        .requestMatchers("/api/rest/auth/**").permitAll()
                        // Only admins can add, edit or delete books, laptops and smartphones
                        .requestMatchers("/books/add", "/books/edit/**", "/books/delete/**",
                                "/laptops/add", "/laptops/edit/**", "/laptops/delete/**",
                                "/smartphones/add", "/smartphones/edit/**", "/smartphones/delete/**").hasRole("ADMIN")
                        // Current-user cart actions: the controller finds the owner from the logged-in user
                        .requestMatchers(HttpMethod.GET, "/api/rest/carts/mine").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/rest/carts/mine/products/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rest/carts/mine/products/*").hasAnyRole("USER", "ADMIN")
                        // Any other cart URL is administrative (it could show someone else's cart)
                        .requestMatchers("/api/rest/carts", "/api/rest/carts/**").hasRole("ADMIN")
                        // REST API: reading (GET) is allowed for USER and ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/rest/**").hasAnyRole("USER", "ADMIN")
                        // REST API: create, update and delete are only for ADMIN.
                        // The narrower rules above must come first because the first matching rule wins
                        .requestMatchers("/api/rest/**").hasRole("ADMIN")
                        // Everything else just needs a logged-in user
                        .anyRequest().authenticated()
                )
                // Without a login, API requests get 401 and the normal pages go to the login page
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/rest/")) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                // Run our JWT filter before the form login filter
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
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

        // CSRF tokens are only needed for browser forms. curl and Swagger do not send one,
        // so CSRF is ignored only for the REST API; the normal pages stay protected.
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/rest/**"));

        return http.build();
    }
}