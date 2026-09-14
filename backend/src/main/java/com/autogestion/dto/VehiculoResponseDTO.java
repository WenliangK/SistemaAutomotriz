package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VehiculoResponseDTO {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private Long clienteId;
    private String clienteNombre;
}
