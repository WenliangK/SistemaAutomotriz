package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class DiagnosticoRequest {
    private Long recepcionId;
    private Long mecanicoId;
    private String descripcion;
}
