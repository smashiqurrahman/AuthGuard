package com.ashiq.AuthGuard.controller;


import com.ashiq.AuthGuard.dto.*;
import com.ashiq.AuthGuard.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {


    private final RoleService roleService;


    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }


    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }


    @PostMapping("/assign-permissions")
    public ResponseEntity<RolePermissionResponse> assign(@RequestBody RolePermissionRequest request) {
        return ResponseEntity.ok(roleService.assignPermissions(request));
    }
}