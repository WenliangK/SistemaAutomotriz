package com.autogestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Para emitir NO se recibe ningún monto: el total sale de la cotización
 * en el backend. Lo que el navegador envía se ignora por seguridad.
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class ComprobanteEmitirRequest {

    @NotNull(message = "Falta la orden de trabajo")
    private Long ordenTrabajoId;

    /** BOLETA o FACTURA */
    @NotNull(message = "Elige boleta o factura")
    private String tipo;

    /** Opcional: si se omite se usa el dueño del vehículo de la OT. */
    private Long clienteId;

    /** DNI, RUC, CE, PASAPORTE o SIN_DOC (boleta chica). */
    private String tipoDoc;
    private String numDoc;
    private String nombre;
    private String direccion;

    /** EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA */
    @NotNull(message = "Elige el método de pago")
    private String metodoPago;

    /** CONTADO o CREDITO (crédito solo en factura). */
    private String formaPago;

    private Boolean registrarEntrega;
    private String observacion;
}
