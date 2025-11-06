package com.ashiq.AuthGuard.helper;

import com.ashiq.AuthGuard.constants.PermissionType;
import com.ashiq.AuthGuard.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PermissionHelper {
    public boolean hasPermission(User user, PermissionType permission) {
        if (user == null || user.getRole() == null || user.getRole().getPermissions() == null) return false;

        return user.getRole().getPermissions().stream()
                .anyMatch(p -> p.getName().equals(permission.name()));
    }

}
