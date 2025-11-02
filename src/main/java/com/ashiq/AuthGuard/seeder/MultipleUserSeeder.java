package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.entity.User;
import com.ashiq.AuthGuard.repository.RoleRepository;
import com.ashiq.AuthGuard.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MultipleUserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedUsers(){
        createUserIfNotExists("admin@example.com", "Admin User", "ROLE_ADMIN");
        createUserIfNotExists("vendor@example.com", "Vendor User", "ROLE_VENDOR");
        createUserIfNotExists("customer@example.com", "Customer User", "ROLE_CUSTOMER");
    }

    private void createUserIfNotExists(String email, String fullName, String roleName) {
        if (userRepository.findByEmail(email).isEmpty()) {
            Optional<Role> roleOpt = roleRepository.findByName(roleName);
            if (roleOpt.isPresent()) {
                User user = User.builder()
                        .fullName(fullName)
                        .email(email)
                        .password(passwordEncoder.encode("password"))
                        .role(roleOpt.get())
                        .enabled(true)
                        .locked(false)
                        .build();
                userRepository.save(user);
            }
        }
    }
}
