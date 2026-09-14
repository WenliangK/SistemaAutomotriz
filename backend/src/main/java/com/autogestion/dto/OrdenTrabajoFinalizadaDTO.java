package com.autogestion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class OrdenTrabajoFinalizadaDTO {
    private Long id;
    private Long cotizacionId;
    private String mecanicoNombre;
    private LocalDateTime fechaCreacion;
    private BigDecimal monto;
    private Boolean tienePago;
    private Boolean tieneEntrega;
}