package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class MovimientoInventarioRequest {
    private Long productoId;
    private String tipo;
    private Integer cantidad;
    private String motivo;
}
