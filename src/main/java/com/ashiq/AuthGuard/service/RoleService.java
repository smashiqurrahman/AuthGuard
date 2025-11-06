package com.ashiq.AuthGuard.service;


import com.ashiq.AuthGuard.dto.*;
import com.ashiq.AuthGuard.entity.*;
import com.ashiq.AuthGuard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoleService {


    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    public RoleResponse createRole(RoleRequest request) {
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        Role role = Role.builder()
                .name(request.getName())
                .permissions(permissions)
                .build();
        role = roleRepository.save(role);
        return map(role);
    }


    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }


    private RoleResponse map(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()))
                .build();
    }


    public RolePermissionResponse assignPermissions(RolePermissionRequest request) {
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow();
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
        role.setPermissions(permissions);
        roleRepository.save(role);
        return RolePermissionResponse.builder()
                .roleName(role.getName())
                .permissions(permissions.stream().map(Permission::getName).collect(Collectors.toSet()))
                .build();
    }
}