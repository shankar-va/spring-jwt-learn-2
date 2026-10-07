package com.example.spring_jwtlearn.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class publicController {

    @GetMapping("/test-auth")
    public String testAuth(Authentication authentication) {

        return "Authenticated as " + authentication.getName() + "\nAuthorities: " + authentication.getAuthorities() + "\nCredentials: " + authentication.getCredentials();
    }

}
