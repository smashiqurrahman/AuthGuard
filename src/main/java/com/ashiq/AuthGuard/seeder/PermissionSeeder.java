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
        // USER permissions
        saveIfNotExists("/api/test/user/view", "GET", PermissionType.USER_VIEW.name(), "auth-service", true);
        saveIfNotExists("/api/test/user/create", "POST", PermissionType.USER_CREATE.name(), "auth-service", true);
        saveIfNotExists("/api/test/user/update", "PUT", PermissionType.USER_UPDATE.name(), "auth-service", true);
        saveIfNotExists("/api/test/user/delete", "DELETE", PermissionType.USER_DELETE.name(), "auth-service", true);

        // ROLE permissions
        saveIfNotExists("/api/test/role/view", "GET", PermissionType.ROLE_VIEW.name(), "auth-service", true);
        saveIfNotExists("/api/test/role/create", "POST", PermissionType.ROLE_CREATE.name(), "auth-service", true);
        saveIfNotExists("/api/test/role/update", "PUT", PermissionType.ROLE_UPDATE.name(), "auth-service", true);
        saveIfNotExists("/api/test/role/delete", "DELETE", PermissionType.ROLE_DELETE.name(), "auth-service", true);

        System.out.println("✅ PermissionSeeder completed successfully!");
    }

    /**
     * Save permission in DB if not already exists.
     *
     * @param endpoint The API endpoint path
     * @param method The HTTP method (GET, POST, etc.)
     * @param name The permission name (Enum string)
     * @param service The service name (e.g., auth-service)
     * @param isOrg Whether it's organization-level permission
     */
    private void saveIfNotExists(String endpoint, String method, String name, String service, boolean isOrg) {
        permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                                .apiEndPoint(endpoint)
                                .method(method)
                                .name(name)
                                .serviceName(service)
                                .isOrgPermission(isOrg)
                                .build()
                ));
    }
}
