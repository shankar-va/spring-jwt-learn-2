package com.example.spring_jwtlearn.security;

import com.example.spring_jwtlearn.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private final User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = user.getRoles()
                                                      .stream()
                                                      .map(role -> new SimpleGrantedAuthority(role.getRole() + ""))
                                                      .collect(Collectors.toSet());
        user.getRoles()
            .stream()
            .flatMap(role -> role.getPrivileges()
                                 .stream())
            .map(privilege -> new SimpleGrantedAuthority(privilege + ""))
            .forEach(authority -> authorities.add(authority));
        return authorities;
    }


    @Override
    public @Nullable String getPassword() {

        return user.getPassword();
    }


    @Override
    public String getUsername() {

        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {

        return user.isAccountNonExpired();
    }


    @Override
    public boolean isAccountNonLocked() {

        return user.isAccountNonLocked();
    }


    @Override
    public boolean isCredentialsNonExpired() {

        return user.isCredentialsNonExpired();
    }


    @Override
    public boolean isEnabled() {

        return user.isEnabled();
    }

}
