package com.example.spring_jwtlearn.service;

import com.example.spring_jwtlearn.Enum.Role;
import com.example.spring_jwtlearn.dto.RegistrationRequest;
import com.example.spring_jwtlearn.mapper.RegistrationRequestMapper;
import com.example.spring_jwtlearn.model.Roles;
import com.example.spring_jwtlearn.model.User;
import com.example.spring_jwtlearn.repository.RoleRepository;
import com.example.spring_jwtlearn.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final RegistrationRequestMapper mapper;


    @Transactional
    public User register(RegistrationRequest request) {

        User user = mapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        Roles userRole = roleRepository.findByRole(Role.ROLE_USER)
                                       .orElseThrow(() ->
                                               new IllegalStateException("ROLE_USER not found")
                                       );

        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }

}