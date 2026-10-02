package com.autogestion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Un movimiento con nombres (nunca la entidad con relaciones perezosas). */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoResponseDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private BigDecimal costoUnitario;
    private Long proveedorId;
    private String proveedorNombre;
    private Long ordenTrabajoId;
    private Long usuarioId;
    private String usuarioNombre;
    private String documentoRef;
    private Integer stockAntes;
    private Integer stockDespues;
    private LocalDateTime fecha;
}
