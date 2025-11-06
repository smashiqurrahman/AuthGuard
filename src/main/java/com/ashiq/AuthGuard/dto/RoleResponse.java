package com.ashiq.AuthGuard.dto;


import lombok.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private Set<String> permissions; // Permission names
}