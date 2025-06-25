package com.ashiq.AuthGuard;

import com.ashiq.AuthGuard.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class AuthGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthGuardApplication.class, args);
	}

}
