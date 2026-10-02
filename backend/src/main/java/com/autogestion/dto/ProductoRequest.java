package com.autogestion.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "Nombre muy largo")
    private String nombre;

    /** REPUESTO o INSUMO */
    @NotBlank(message = "El tipo es obligatorio (REPUESTO o INSUMO)")
    private String tipo;

    /** Precio de venta con IGV. */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precioUnitario;

    /** Último costo de compra (para margen y valorización). */
    @DecimalMin(value = "0.0", message = "El costo no puede ser negativo")
    private BigDecimal costoUnitario;

    /** NIU (bienes) o ZZ (servicios). */
    private String unidadMedida;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockActual;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
}
