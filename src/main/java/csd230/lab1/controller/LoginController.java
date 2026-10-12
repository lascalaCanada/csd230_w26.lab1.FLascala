package csd230.lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Only shows the login page. The login itself (POST /login) is handled by Spring Security.
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}