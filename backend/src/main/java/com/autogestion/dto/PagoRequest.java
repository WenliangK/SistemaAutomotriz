package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class PagoRequest {
    private Long ordenTrabajoId;
    private Double monto;
}
