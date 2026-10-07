package com.example.spring_jwtlearn.controller;

import com.example.spring_jwtlearn.dto.AuthResponse;
import com.example.spring_jwtlearn.dto.LoginRequest;
import com.example.spring_jwtlearn.dto.RegistrationRequest;
import com.example.spring_jwtlearn.mapper.RegistrationRequestMapper;
import com.example.spring_jwtlearn.model.User;
import com.example.spring_jwtlearn.repository.UserRepository;
import com.example.spring_jwtlearn.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final User user;

    private final RegistrationRequestMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest userRequest) {

        User user = mapper.toEntity(userRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Optional.of(userRepository.save(user))
                       .orElseThrow(() -> new UsernameNotFoundException("Cannot register user!!!..."));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @GetMapping("/test-auth")
    public String testAuth(Authentication authentication) {

        return "Authenticated as " + authentication.getName() + "\nAuthorities: " + authentication.getAuthorities() + "\nCredentials: " + authentication.getCredentials();
    }

}
