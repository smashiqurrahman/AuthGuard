package com.ashiq.AuthGuard.controller;

import com.ashiq.AuthGuard.constants.EndPointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndPointConstants.TEST_BASE)
public class TestPermissionController {

    // ---------- USER Permissions ----------
    @GetMapping(EndPointConstants.USER_VIEW)
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<String> viewUser() {
        return ResponseEntity.ok("✅ USER_VIEW permission passed!");
    }

    @PostMapping(EndPointConstants.USER_CREATE)
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<String> createUser() {
        return ResponseEntity.ok("✅ USER_CREATE permission passed!");
    }

    @PutMapping(EndPointConstants.USER_UPDATE)
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<String> updateUser() {
        return ResponseEntity.ok("✅ USER_UPDATE permission passed!");
    }

    @DeleteMapping(EndPointConstants.USER_DELETE)
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<String> deleteUser() {
        return ResponseEntity.ok("✅ USER_DELETE permission passed!");
    }

    // ---------- ROLE Permissions ----------
    @GetMapping(EndPointConstants.ROLE_VIEW)
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<String> viewRole() {
        return ResponseEntity.ok("✅ ROLE_VIEW permission passed!");
    }

    @PostMapping(EndPointConstants.ROLE_CREATE)
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<String> createRole() {
        return ResponseEntity.ok("✅ ROLE_CREATE permission passed!");
    }

    @PutMapping(EndPointConstants.ROLE_UPDATE)
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<String> updateRole() {
        return ResponseEntity.ok("✅ ROLE_UPDATE permission passed!");
    }

    @DeleteMapping(EndPointConstants.ROLE_DELETE)
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<String> deleteRole() {
        return ResponseEntity.ok("✅ ROLE_DELETE permission passed!");
    }

    // ---------- Role-based access test ----------
    @GetMapping(EndPointConstants.ADMIN_ONLY)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("🔐 ROLE_ADMIN access granted!");
    }

    @GetMapping(EndPointConstants.USER_ONLY)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> userOnly() {
        return ResponseEntity.ok("🔐 ROLE_USER access granted!");
    }
}
