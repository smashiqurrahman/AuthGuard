package com.ashiq.AuthGuard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponse {
    private Long id;
    private String name;
}
