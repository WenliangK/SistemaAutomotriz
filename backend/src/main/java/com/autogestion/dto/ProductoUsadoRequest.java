package com.autogestion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoUsadoRequest {
    @NotNull(message = "Elige el repuesto o insumo")
    private Long productoId;

    @NotNull(message = "Indica la cantidad")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidadUsada;
}
