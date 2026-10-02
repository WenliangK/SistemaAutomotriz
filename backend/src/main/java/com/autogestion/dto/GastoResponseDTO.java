package com.autogestion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GastoResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String categoria;
    private String categoriaEtiqueta;
    private String descripcion;
    private BigDecimal monto;
    private Long proveedorId;
    private String proveedorNombre;
    private String documentoRef;
    private String registradoPorNombre;
}
