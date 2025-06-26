package com.ashiq.AuthGuard.service;

import com.ashiq.AuthGuard.dto.AuthResponse;
import com.ashiq.AuthGuard.dto.LoginRequest;
import com.ashiq.AuthGuard.dto.RegisterRequest;
import com.ashiq.AuthGuard.dto.SetPasswordRequest;
import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.entity.User;
import com.ashiq.AuthGuard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
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

        return "Registration successful. Please check your email to set your password.";
    }


    public String setPassword(SetPasswordRequest request) {
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

        return "Password set and account activated.";
    }

    public AuthResponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        }catch (Exception ex){
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

        return new AuthResponse(accessToken, refreshToken);
    }
}
