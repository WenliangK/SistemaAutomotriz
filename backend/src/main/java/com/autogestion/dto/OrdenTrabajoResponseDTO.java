package com.autogestion.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdenTrabajoResponseDTO {
    private Long id;
    private Long cotizacionId;
    private Long mecanicoId;
    private String mecanicoNombre;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaFin;
    private String vehiculoPlaca;
    private String clienteNombre;
}