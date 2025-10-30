package com.ashiq.AuthGuard.controller;

import com.ashiq.AuthGuard.constants.EndPointConstants;
import com.ashiq.AuthGuard.dto.AuthResponse;
import com.ashiq.AuthGuard.dto.LoginRequest;
import com.ashiq.AuthGuard.dto.RegisterRequest;
import com.ashiq.AuthGuard.dto.SetPasswordRequest;
import com.ashiq.AuthGuard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndPointConstants.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordRequest request) {
        return authService.setPassword(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     *This API will generate token by refresh token.
     * @param httpHeaders - headers
     * @return Response
     * @author Ashiqur Rahman
     * @since 17 July 2025
     */
    @Operation(summary = "Generate access token", description = "Generate token for the user by refresh Token access the api endpoint")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Generate Access Token", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthController.class)))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "User not created for internal error ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class))))})
    @GetMapping(path = EndPointConstants.AUTH_REGENERATE_TOKEN, produces = "application/json")
    public ResponseEntity<?> regenerateToken(@RequestHeader HttpHeaders httpHeaders){
        return authService.regenerateToken(httpHeaders);
    }



}
