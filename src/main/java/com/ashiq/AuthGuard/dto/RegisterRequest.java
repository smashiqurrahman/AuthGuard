package com.ashiq.AuthGuard.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
}
