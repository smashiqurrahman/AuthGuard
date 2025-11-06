package com.ashiq.AuthGuard.seeder;

import com.ashiq.AuthGuard.constants.PermissionType;
import com.ashiq.AuthGuard.constants.RoleType;
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

        // ✅ Super Admin → All Permissions
        createRoleIfNotExists(RoleType.ROLE_SUPER_ADMIN.name(), new HashSet<>(allPermissions));

        // ✅ Admin → User & Role management
        createRoleIfNotExists(RoleType.ROLE_ADMIN.name(), Set.of(
                getPermission(PermissionType.USER_VIEW),
                getPermission(PermissionType.USER_CREATE),
                getPermission(PermissionType.USER_UPDATE),
                getPermission(PermissionType.ROLE_VIEW),
                getPermission(PermissionType.ROLE_CREATE),
                getPermission(PermissionType.ROLE_UPDATE)
        ));

        // ✅ Support → View-only access
        createRoleIfNotExists(RoleType.ROLE_SUPPORT.name(), Set.of(
                getPermission(PermissionType.USER_VIEW),
                getPermission(PermissionType.ROLE_VIEW)
        ));

        // ✅ Vendor → Only view
        createRoleIfNotExists(RoleType.ROLE_VENDOR.name(), Set.of(
                getPermission(PermissionType.USER_VIEW)
        ));

        // ✅ Customer → Minimal access (optional)
        createRoleIfNotExists(RoleType.ROLE_CUSTOMER.name(), Set.of(
                getPermission(PermissionType.USER_VIEW)
        ));

        System.out.println("✅ RoleSeeder completed successfully!");
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
        return permissionRepository.findByName(type.name())
                .orElseThrow(() -> new RuntimeException("❌ Permission not found: " + type.name()));
    }
}
