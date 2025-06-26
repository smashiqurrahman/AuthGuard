package com.ashiq.AuthGuard.controller;

import com.ashiq.AuthGuard.dto.AuthResponse;
import com.ashiq.AuthGuard.dto.LoginRequest;
import com.ashiq.AuthGuard.dto.RegisterRequest;
import com.ashiq.AuthGuard.dto.SetPasswordRequest;
import com.ashiq.AuthGuard.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody SetPasswordRequest request) {
        return ResponseEntity.ok(authService.setPassword(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
