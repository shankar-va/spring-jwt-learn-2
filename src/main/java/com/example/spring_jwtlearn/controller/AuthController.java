package com.example.spring_jwtlearn.controller;

import com.example.spring_jwtlearn.dto.AuthResponse;
import com.example.spring_jwtlearn.dto.LoginRequest;
import com.example.spring_jwtlearn.dto.RegistrationRequest;
import com.example.spring_jwtlearn.model.RefreshToken;
import com.example.spring_jwtlearn.model.User;
import com.example.spring_jwtlearn.security.CustomUserDetails;
import com.example.spring_jwtlearn.security.jwt.JwtService;
import com.example.spring_jwtlearn.security.jwt.refreshToken.RefreshTokenService;
import com.example.spring_jwtlearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;


    private final AuthService authService;


    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request) {

        return authService.register(request);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser());
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", refreshToken)
                                                      .httpOnly(true)
                                                      .secure(true)
                                                      .path("/auth/refresh-token")
                                                      .maxAge(60 * 60 * 24 * 7)
                                                      .sameSite("Strict")
                                                      .build();

        return ResponseEntity.ok()
                             .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                             .body(new AuthResponse(accessToken));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refresh-token") String rawToken) {

        RefreshToken validToken = refreshTokenService.verifyByRawToken(rawToken);
        String newAccessToken = jwtService.generateToken(new CustomUserDetails(validToken.getUser()));
        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

}
