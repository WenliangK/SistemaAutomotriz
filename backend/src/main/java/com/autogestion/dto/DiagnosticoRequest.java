package com.autogestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class DiagnosticoRequest {
    @NotNull private Long recepcionId;
    @NotNull private Long mecanicoId;
    @NotNull private String descripcion;
}
