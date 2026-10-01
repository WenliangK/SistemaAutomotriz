package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class OrdenTrabajoRequest {
    private Long cotizacionId;
    private Long mecanicoId;
}
