package com.example.spring_jwtlearn.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @SneakyThrows
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO BEARER TOKEN");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN RECEIVED");

        String userEmail = jwtService.extractEmail(token);

        System.out.println("USER EMAIL = " + userEmail);

        if (userEmail != null &&
                SecurityContextHolder.getContext()
                                     .getAuthentication() == null) {

            System.out.println("PASSED AUTHENTICATION CONDITION");

            if (jwtService.isValidToken(token)) {

                System.out.println("TOKEN IS VALID");

                List<SimpleGrantedAuthority> authorities =
                        jwtService.extractAuthorities(token);

                System.out.println("AUTHORITIES = " + authorities);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                                     .setAuthentication(authToken);

                System.out.println("JWT AUTHENTICATION SET");
            } else {
                System.out.println("TOKEN IS INVALID");
            }
        }
        filterChain.doFilter(request, response);
    }

}
