package com.autogestion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * ENTRADA exige costoUnitario (lo pagado). AJUSTE_NEGATIVO y MERMA exigen
 * motivo. CONSUMO exige ordenTrabajoId (lo pone el servicio de OT).
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class MovimientoInventarioRequest {

    @NotNull(message = "Falta el producto")
    private Long productoId;

    /** ENTRADA, CONSUMO, AJUSTE_POSITIVO, AJUSTE_NEGATIVO, MERMA */
    @NotNull(message = "Falta el tipo de movimiento")
    private String tipo;

    @NotNull(message = "Falta la cantidad")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    private String motivo;

    /** En ENTRADA: precio pagado por unidad. */
    private BigDecimal costoUnitario;

    private Long proveedorId;
    private Long ordenTrabajoId;
    private String documentoRef;
}
