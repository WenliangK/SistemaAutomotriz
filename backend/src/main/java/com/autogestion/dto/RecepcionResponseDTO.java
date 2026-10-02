package com.autogestion.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecepcionResponseDTO {
    private Long id;
    private String problemaReportado;
    private LocalDateTime fechaIngreso;
    private String estado;
    private Long vehiculoId;
    private String vehiculoPlaca;
    private Long clienteId;
    private String clienteNombre;
    private String nivelCombustible;
    private String danosPrevios;
    private String accesorios;
    private Integer kilometraje;
}
