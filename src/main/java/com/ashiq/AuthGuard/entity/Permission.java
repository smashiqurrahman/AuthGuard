package com.ashiq.AuthGuard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_end_point")
    private String apiEndPoint;

    @Column(name = "method")
    private String method;

    @Column(name = "permission_name")
    private String name;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "is_org_permission")
    private boolean isOrgPermission;
}
