package com.autogestion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class GastoRequest {

    private LocalDate fecha;

    /** ALQUILER, SERVICIOS, SUELDOS, HERRAMIENTAS, LIMPIEZA, TRANSPORTE, OTROS */
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "Describe el gasto")
    @Size(max = 250, message = "Descripción muy larga")
    private String descripcion;

    @NotNull(message = "Falta el monto")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    private Long proveedorId;
    private String documentoRef;
}
