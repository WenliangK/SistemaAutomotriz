package com.autogestion.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DiagnosticoResponseDTO {
    private Long id;
    private String descripcion;
    private LocalDateTime fecha;
    private Long recepcionId;
    private Long mecanicoId;
    private String mecanicoNombre;
}
