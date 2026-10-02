package com.autogestion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecepcionRequest {
    @NotNull(message = "Falta el vehículo") private Long vehiculoId;
    @NotNull(message = "Describe el problema reportado")
    @Size(min = 5, max = 2000, message = "Describe el problema con al menos 5 letras")
    private String problemaReportado;
}
