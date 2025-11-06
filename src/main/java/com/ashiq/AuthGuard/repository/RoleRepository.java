package com.ashiq.AuthGuard.repository;

import com.ashiq.AuthGuard.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = "permissions") // This is the key part
    Optional<Role> findWithPermissionsById(Long id);
}
