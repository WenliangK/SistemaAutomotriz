package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoRequest {
    private String nombre;
    private String tipo;
    private Double precioUnitario;
    private Integer stockActual;
    private Integer stockMinimo;
}
