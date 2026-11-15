package csd230.lab1.controller;

import csd230.lab1.auth.JwtUtil;
import csd230.lab1.dtos.requests.LoginReq;
import csd230.lab1.dtos.responses.ErrorRes;
import csd230.lab1.dtos.responses.LoginRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login endpoint for the REST API: the client sends username and password
 * and receives a JWT to use in the next requests.
 */
@RestController
@RequestMapping("/api/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            // Checks the user in the database (CustomUserDetailsService) and the BCrypt password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
            );

            String username = authentication.getName();

            // The roles from the database go inside the token
            String token = jwtUtil.createToken(username, authentication.getAuthorities());

            return ResponseEntity.ok(new LoginRes(username, token));
        } catch (BadCredentialsException e) {
            // Wrong username or password
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password"));
        }
    }
}
