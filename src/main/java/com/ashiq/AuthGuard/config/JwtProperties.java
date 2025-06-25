package com.ashiq.AuthGuard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private Duration accessTokenExpiration;
    private Duration refreshTokenExpiration;
    private Duration verificationTokenExpiration;
}