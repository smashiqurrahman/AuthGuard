package com.ashiq.AuthGuard.dto;


import lombok.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionRequest {
    private Long roleId;
    private Set<Long> permissionIds;
}