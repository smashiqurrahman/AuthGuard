package com.ashiq.AuthGuard.service;

import com.ashiq.AuthGuard.dto.*;
import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.entity.User;
import com.ashiq.AuthGuard.helper.CommonFunction;
import com.ashiq.AuthGuard.repository.RoleRepository;
import com.ashiq.AuthGuard.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.*;

import java.util.*;

import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class AuthService implements CommonFunction {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public ResponseEntity<?> register(RegisterRequest request) {
        Response response = new Response();

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>(getErrorResponse("Email already exists."), HttpStatus.CONFLICT);
        }

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(customerRole)
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

        Claims claims = jwtService.extractAllClaims(request.getToken());
        String email = claims.get("email", String.class);

        if (email == null) throw new RuntimeException("Token missing email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(request.getToken(), user)) {
            throw new RuntimeException("Invalid or expired token");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<>(getSuccessResponse("Password set and account activated."), HttpStatus.OK);
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {

        logger.info("Attempting to authenticate user with email: {} ", loginRequest.getEmail());

        Response<AuthResponse> response = new Response<>();
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

        // 🔥 Step 1: collect authorities (roles + permissions)
        Set<String> authorities = new HashSet<>();
        authorities.add(user.getRole().getName()); // e.g. ROLE_ADMIN
        user.getRole().getPermissions().forEach(p ->
                authorities.add(p.getName().name()) // e.g. USER_VIEW, USER_CREATE
        );

        // 🔥 Step 2: include authorities inside claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getName());
        claims.put("authorities", authorities); // ✅ new important line

        // 🔥 Step 3: generate JWT tokens with authorities
        String accessToken = jwtService.generateAccessToken(claims);
        String refreshToken = jwtService.generateRefreshToken(claims);

        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);

        response.setObj(authResponse);

        return new ResponseEntity<>(getSuccessResponse("Login successful", response), HttpStatus.OK);
    }

    public ResponseEntity<?> regenerateToken(HttpHeaders httpHeaders) {
        String token = Objects.requireNonNull(httpHeaders.get("Authorization")).get(0);
        String jwt = token.substring(7);

        String email = jwtService.extractEmailFromToken(jwt);

        User user = userRepository.findByEmailAndEnabledTrueAndLockedFalse(email);

        if (ObjectUtils.isEmpty(user)) {
            return new ResponseEntity<>(getErrorResponse("User not found or account is not active."), HttpStatus.NOT_FOUND);
        }

        if (!jwtService.isTokenValid(jwt, user)) {
            return new ResponseEntity<>(getErrorResponse("Invalid or expired token."), HttpStatus.UNAUTHORIZED);
        }

        // 🔥 Step 1: include authorities again
        Set<String> authorities = new HashSet<>();
        authorities.add(user.getRole().getName());
        user.getRole().getPermissions().forEach(p ->
                authorities.add(p.getName().name())
        );

        // 🔥 Step 2: build claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getName());
        claims.put("authorities", authorities); // ✅ important

        // 🔥 Step 3: generate fresh tokens
        String newAccessToken = jwtService.generateAccessToken(claims);
        String newRefreshToken = jwtService.generateRefreshToken(claims);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newAccessToken);
        authResponse.setRefreshToken(newRefreshToken);

        Response<AuthResponse> response = new Response<>();
        response.setObj(authResponse);

        return new ResponseEntity<>(getSuccessResponse("Token regenerated successfully", response), HttpStatus.OK);
    }


}
