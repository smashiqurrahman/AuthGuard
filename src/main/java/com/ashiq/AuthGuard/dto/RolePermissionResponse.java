package com.ashiq.AuthGuard.dto;


import lombok.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionResponse {
    private String roleName;
    private Set<String> permissions;
}