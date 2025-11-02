package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.constants.RoleType;
import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (RoleType type : RoleType.values()) {
            roleRepository.findByName(type.name()).orElseGet(() -> {
                return roleRepository.save(Role.builder().name(type.name()).build());
            });
        }
    }
}

