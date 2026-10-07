package com.example.spring_jwtlearn.security;

import com.example.spring_jwtlearn.model.User;
import com.example.spring_jwtlearn.repository.RoleRepository;
import com.example.spring_jwtlearn.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findUserWithRoles(email)
                                  .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password!!!..."));
        roleRepository.findRolesWithPrivileges(user.getRoles());
        return new CustomUserDetails(user);
    }

}
