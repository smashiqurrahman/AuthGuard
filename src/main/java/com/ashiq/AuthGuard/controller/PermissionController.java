package com.ashiq.AuthGuard.controller;

import com.ashiq.AuthGuard.dto.PermissionRequest;
import com.ashiq.AuthGuard.dto.PermissionResponse;
import com.ashiq.AuthGuard.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAll() {
        return ResponseEntity.ok(permissionService.getAll());
    }
}
