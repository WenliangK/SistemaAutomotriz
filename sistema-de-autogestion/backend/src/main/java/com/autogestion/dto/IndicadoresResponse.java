package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class IndicadoresResponse {
    private Long otCompletadasMes;
    private Double tiempoPromedioHoras;
    private Double ingresosMes;
    private Long productosBajoStock;
}
