package com.ashiq.AuthGuard.service;

import com.ashiq.AuthGuard.dto.*;
import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.entity.User;
import com.ashiq.AuthGuard.helper.CommonFunction;
import com.ashiq.AuthGuard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements CommonFunction {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public ResponseEntity<?> register(RegisterRequest request) {
        Response response = new Response();

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>(getErrorResponse("Email already exists."), HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(Role.CUSTOMER)
                .enabled(false)
                .locked(false)
                .build();

        userRepository.save(user);

        // build JWT token with email + purpose
        Map<String, Object> claims = Map.of(
                "email", user.getEmail(),
                "purpose", "verify"
        );

        String token = jwtService.generateVerificationToken(claims);
        String link = "http://localhost:3000/set-password?token=" + token;

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Verify your email and set your password",
                emailService.buildVerificationEmail(link)
        );

        return new ResponseEntity<>(getSuccessResponse("Registration successful. Please check your email to set your password."), HttpStatus.CREATED);
    }


    public ResponseEntity<?> setPassword(SetPasswordRequest request) {
        Response response = new Response();

        if (!jwtService.isTokenValid(request.getToken())) {
            throw new RuntimeException("Invalid or expired token");
        }

        Claims claims = jwtService.extractAllClaims(request.getToken());

        String email = claims.get("email", String.class);
        if (email == null) throw new RuntimeException("Token missing email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<>(getSuccessResponse("Password set and account activated."), HttpStatus.OK);
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Response<AuthResponse> response = new Response();
        AuthResponse authResponse = new AuthResponse();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> claims = Map.of(
                "email", user.getEmail(),
                "role", user.getRole().name()
        );

        String accessToken = jwtService.generateAccessToken(claims);
        String refreshToken = jwtService.generateRefreshToken(claims);

        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        response.setObj(authResponse);
        return new ResponseEntity<>(getSuccessResponse("Login successful", response), HttpStatus.OK);
    }
}
