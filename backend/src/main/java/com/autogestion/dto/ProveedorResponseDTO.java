package com.autogestion.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProveedorResponseDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String telefono;
    private String email;
    private Boolean activo;
    private java.time.LocalDateTime creadoEn;
}