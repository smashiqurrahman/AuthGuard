package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.entity.Permission;
import com.ashiq.AuthGuard.repository.PermissionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {

    private final PermissionRepository permissionRepository;

    @PostConstruct
    public void seedPermission() {
        List<String> permissions = Arrays.asList("USER_VIEW", "USER_CREATE", "USER_UPDATE", "USER_DELETE", "ROLE_VIEW", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE");

        for (String permissionName : permissions) {
            permissionRepository.findByName(permissionName).orElseGet(() -> {
                Permission permission = Permission.builder().name(permissionName).build();
                return permissionRepository.save(permission);
            });
        }
    }
}


