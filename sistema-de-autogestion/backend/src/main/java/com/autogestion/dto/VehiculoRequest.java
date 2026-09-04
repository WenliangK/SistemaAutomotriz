package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class VehiculoRequest {
    private Long clienteId;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
}
