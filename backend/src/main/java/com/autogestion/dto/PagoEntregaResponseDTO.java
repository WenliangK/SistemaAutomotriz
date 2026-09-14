package com.autogestion.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoEntregaResponseDTO {
    private Long id;
    private Long ordenTrabajoId;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaEntrega;
}