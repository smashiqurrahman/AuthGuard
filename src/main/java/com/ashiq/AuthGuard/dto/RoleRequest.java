package com.ashiq.AuthGuard.dto;


import lombok.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    private String name; // Role name
    private Set<Long> permissionIds; // List of permission IDs to assign
}