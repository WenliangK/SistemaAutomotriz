package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}
