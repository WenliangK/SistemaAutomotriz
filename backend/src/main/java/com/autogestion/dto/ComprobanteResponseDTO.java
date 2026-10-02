package com.autogestion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Todo lo que el frontend necesita para mostrar, imprimir y descargar. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ComprobanteResponseDTO {

    private Long id;
    private Long ordenTrabajoId;
    private String tipo;
    private String codigoSunat;
    private String serie;
    private Integer numero;
    private String folio;
    private LocalDateTime fechaEmision;
    private String moneda;
    private String formaPago;
    private String metodoPago;
    private String estado;
    private String motivoAnulacion;

    private String emisorRuc;
    private String emisorRazonSocial;
    private String emisorNombreComercial;
    private String emisorDireccion;
    private String emisorUbigeo;
    private String emisorTelefono;
    private String emisorEmail;

    private Long clienteId;
    private String clienteTipoDoc;
    private String clienteNumDoc;
    private String clienteNombre;
    private String clienteDireccion;

    private BigDecimal totalGravado;
    private BigDecimal igv;
    private BigDecimal total;
    private String totalLetras;
    private String observacion;
    private String qrContenido;
    private String emitidoPorNombre;
    private List<DetalleDTO> detalle;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DetalleDTO {
        private Integer item;
        private String codigo;
        private String descripcion;
        private String unidadMedida;
        private BigDecimal cantidad;
        private BigDecimal valorUnitario;
        private BigDecimal precioUnitario;
        private BigDecimal valorVenta;
        private BigDecimal igv;
        private BigDecimal importeTotal;
    }
}
