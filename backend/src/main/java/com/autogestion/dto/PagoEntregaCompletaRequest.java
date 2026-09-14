package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class PagoEntregaCompletaRequest {

    private Long ordenTrabajoId;
    private Double monto;
    private Boolean registrarEntrega;
}