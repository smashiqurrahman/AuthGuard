package com.ashiq.AuthGuard.entity;//package com.ashiq.AuthGuard.entity;
//
//public enum Role {
//    ROLE_SUPER_ADMIN,
//    ROLE_ADMIN,
//    ROLE_VENDOR,
//    ROLE_CUSTOMER,
//    ROLE_DELIVERY,
//    ROLE_SUPPORT,
//    ROLE_FINANCE,
//    ROLE_MARKETING,
//    ROLE_WAREHOUSE,
//    ROLE_QUALITY,
//    ROLE_REGION_MANAGER
//}


import com.ashiq.AuthGuard.entity.Permission;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
