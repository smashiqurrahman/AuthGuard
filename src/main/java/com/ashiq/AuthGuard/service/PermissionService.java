package com.ashiq.AuthGuard.service;

import com.ashiq.AuthGuard.dto.PermissionRequest;
import com.ashiq.AuthGuard.dto.PermissionResponse;
import com.ashiq.AuthGuard.entity.Permission;
import com.ashiq.AuthGuard.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = Permission.builder()
                .name(request.getName())
                .build();

        return map(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private PermissionResponse map(Permission p) {
        return PermissionResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .build();
    }
}
