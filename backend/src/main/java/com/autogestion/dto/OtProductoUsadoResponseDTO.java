package com.autogestion.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OtProductoUsadoResponseDTO {
    private Long id;
    private Long ordenTrabajoId;
    private Long productoId;
    private String productoNombre;
    private Integer cantidadUsada;
}