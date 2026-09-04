package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecepcionRequest {
    private Long vehiculoId;
    private String problemaReportado;
}
