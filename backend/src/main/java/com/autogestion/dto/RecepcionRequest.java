package com.autogestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecepcionRequest {
    @NotNull private Long vehiculoId;
    @NotNull private String problemaReportado;
}
