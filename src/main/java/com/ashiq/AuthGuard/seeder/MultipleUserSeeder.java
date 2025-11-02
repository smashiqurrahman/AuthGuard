package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.entity.User;
import com.ashiq.AuthGuard.repository.RoleRepository;
import com.ashiq.AuthGuard.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(3)
@RequiredArgsConstructor
public class MultipleUserSeeder implements CommandLineRunner {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Admins
        createUser("admin1@example.com", "Admin One", "ROLE_ADMIN");
        createUser("admin2@example.com", "Admin Two", "ROLE_ADMIN");
        createUser("ashiq.admin@example.com", "Ashiq Admin", "ROLE_ADMIN");

        // Regular users
        createUser("user1@example.com", "User One", "ROLE_USER");
        createUser("user2@example.com", "User Two", "ROLE_USER");
        createUser("user3@example.com", "User Three", "ROLE_USER");

        // Managers
        createUser("manager1@example.com", "Manager One", "ROLE_MANAGER");
        createUser("manager2@example.com", "Manager Two", "ROLE_MANAGER");

        // Support
        createUser("support1@example.com", "Support One", "ROLE_SUPPORT");
        createUser("support2@example.com", "Support Two", "ROLE_SUPPORT");

        // Vendors
        createUser("vendor1@example.com", "Vendor One", "ROLE_VENDOR");
        createUser("vendor2@example.com", "Vendor Two", "ROLE_VENDOR");

        // Mixed
        createUser("rifat@example.com", "Rifat Vai", "ROLE_MANAGER");
        createUser("pallob@example.com", "Pallob Mia", "ROLE_SUPPORT");
        createUser("guest@example.com", "Guest User", "ROLE_USER");
    }

    private void createUser(String email, String name, String roleName) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {}, // do nothing
                () -> roleRepository.findByName(roleName).ifPresent(role -> {
                    User user = User.builder()
                            .fullName(name)
                            .email(email)
                            .password(passwordEncoder.encode("password"))
                            .role(role)
                            .enabled(true)
                            .locked(false)
                            .build();
                    userRepository.save(user);
                    System.out.println("✅ User created: " + email);
                })
        );
    }
}
