package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.constants.PermissionType;
import com.ashiq.AuthGuard.entity.Permission;
import com.ashiq.AuthGuard.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        for (PermissionType type : PermissionType.values()) {
            permissionRepository.findByName(type)
                    .orElseGet(() -> permissionRepository.save(
                            Permission.builder().name(type).build()
                    ));
        }
        System.out.println("✅ PermissionSeeder completed");
    }
}
