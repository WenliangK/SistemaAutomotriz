package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoUsadoRequest {
    private Long productoId;
    private Integer cantidadUsada;
}
