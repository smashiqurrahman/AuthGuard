package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.constants.PermissionType;
import com.ashiq.AuthGuard.entity.Permission;
import com.ashiq.AuthGuard.entity.Role;
import com.ashiq.AuthGuard.repository.PermissionRepository;
import com.ashiq.AuthGuard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Order(2)
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        List<Permission> allPermissions = permissionRepository.findAll();

        // ADMIN → all permission
        createRoleIfNotExists("ROLE_ADMIN", new HashSet<>(allPermissions));

        // USER → USER_VIEW only
        createRoleIfNotExists("ROLE_USER", Set.of(getPermission(PermissionType.USER_VIEW)));

        // MANAGER → USER_VIEW, USER_UPDATE
        createRoleIfNotExists("ROLE_MANAGER", Set.of(
                getPermission(PermissionType.USER_VIEW),
                getPermission(PermissionType.USER_UPDATE)
        ));

        // SUPPORT → USER_VIEW, ROLE_VIEW
        createRoleIfNotExists("ROLE_SUPPORT", Set.of(
                getPermission(PermissionType.USER_VIEW),
                getPermission(PermissionType.ROLE_VIEW)
        ));

        // VENDOR → USER_VIEW only
        createRoleIfNotExists("ROLE_VENDOR", Set.of(getPermission(PermissionType.USER_VIEW)));
    }

    private void createRoleIfNotExists(String roleName, Set<Permission> permissions) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = Role.builder()
                    .name(roleName)
                    .permissions(permissions)
                    .build();
            roleRepository.save(role);
            System.out.println("✅ Role created: " + roleName);
        }
    }

    private Permission getPermission(PermissionType type) {
        return permissionRepository.findByName(type)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + type));
    }
}
