package com.ashiq.AuthGuard.dto;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String token;
    private String password;
}
