package com.ashiq.AuthGuard.controller;

import com.ashiq.AuthGuard.constants.EndPointConstants;
import com.ashiq.AuthGuard.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndPointConstants.TEST_BASE)
public class TestPermissionController {

    // ---------- USER Permissions ----------
    @GetMapping(EndPointConstants.USER_VIEW)
    public ResponseEntity<String> viewUser() {
        if (!SecurityUtil.hasPermission("USER_VIEW")) {
            return new ResponseEntity<>("❌ USER_VIEW permission required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("✅ USER_VIEW granted");
    }

    @PostMapping(EndPointConstants.USER_CREATE)
    public ResponseEntity<String> createUser() {
        if (!SecurityUtil.hasPermission("USER_CREATE")) {
            return new ResponseEntity<>("❌ USER_CREATE permission required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("✅ USER_CREATE granted");
    }

    // ---------- ROLE Permissions ----------
    @GetMapping(EndPointConstants.ROLE_VIEW)
    public ResponseEntity<String> viewRole() {
        if (!SecurityUtil.hasPermission("ROLE_VIEW")) {
            return new ResponseEntity<>("❌ ROLE_VIEW permission required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("✅ ROLE_VIEW granted");
    }

    @PostMapping(EndPointConstants.ROLE_CREATE)
    public ResponseEntity<String> createRole() {
        if (!SecurityUtil.hasPermission("ROLE_CREATE")) {
            return new ResponseEntity<>("❌ ROLE_CREATE permission required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("✅ ROLE_CREATE granted");
    }

    // ---------- Role-based testing ----------
    @GetMapping(EndPointConstants.ADMIN_ONLY)
    public ResponseEntity<String> adminOnly() {
        if (!SecurityUtil.hasRole("ROLE_ADMIN")) {
            return new ResponseEntity<>("❌ ROLE_ADMIN required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("🔐 ROLE_ADMIN granted");
    }

    @GetMapping(EndPointConstants.USER_ONLY)
    public ResponseEntity<String> userOnly() {
        if (!SecurityUtil.hasRole("ROLE_USER")) {
            return new ResponseEntity<>("❌ ROLE_USER required", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("🔐 ROLE_USER granted");
    }
}
