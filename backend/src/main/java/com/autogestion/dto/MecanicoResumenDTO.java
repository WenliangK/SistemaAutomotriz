package com.autogestion.dto;

import lombok.*;

/** Mecánico para los selects + carga de trabajo (para sugerir al menos ocupado). */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MecanicoResumenDTO {
    private Long id;
    private String nombreCompleto;
    private String especialidad;
    private String especialidadEtiqueta;
    private Long otActivas;
    private Long otFinalizadasMes;
}
