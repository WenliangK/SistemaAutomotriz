package com.autogestion.service.comprobante;

import com.autogestion.dto.ComprobanteResponseDTO;
import com.autogestion.entity.Comprobante;

import java.util.List;

/**
 * SRP: única razón de cambio = la forma del DTO que ve el frontend.
 * Clase pura y sin estado: el mapeo no necesita transacción ni repositorios.
 */
public final class ComprobanteMapper {

    private ComprobanteMapper() { }

    public static ComprobanteResponseDTO toDTO(Comprobante c) {
        return ComprobanteResponseDTO.builder()
                .id(c.getId())
                .ordenTrabajoId(c.getOrdenTrabajo().getId())
                .tipo(c.getTipo().name())
                .codigoSunat(c.getCodigoSunat())
                .serie(c.getSerie())
                .numero(c.getNumero())
                .folio(c.folio())
                .fechaEmision(c.getFechaEmision())
                .moneda(c.getMoneda())
                .formaPago(c.getFormaPago().name())
                .metodoPago(c.getMetodoPago().name())
                .estado(c.getEstado().name())
                .motivoAnulacion(c.getMotivoAnulacion())
                .emisorRuc(c.getEmisorRuc())
                .emisorRazonSocial(c.getEmisorRazonSocial())
                .emisorNombreComercial(c.getEmisorNombreComercial())
                .emisorDireccion(c.getEmisorDireccion())
                .emisorUbigeo(c.getEmisorUbigeo())
                .emisorTelefono(c.getEmisorTelefono())
                .emisorEmail(c.getEmisorEmail())
                .clienteId(c.getCliente() != null ? c.getCliente().getId() : null)
                .clienteTipoDoc(c.getClienteTipoDoc())
                .clienteNumDoc(c.getClienteNumDoc())
                .clienteNombre(c.getClienteNombre())
                .clienteDireccion(c.getClienteDireccion())
                .totalGravado(c.getTotalGravado())
                .igv(c.getIgv())
                .total(c.getTotal())
                .totalLetras(c.getTotalLetras())
                .observacion(c.getObservacion())
                .qrContenido(c.qrContenido())
                .emitidoPorNombre(c.getEmitidoPor() != null ? c.getEmitidoPor().getNombre() : null)
                .detalle(c.getDetalle() == null ? List.of() : c.getDetalle().stream()
                        .map(d -> ComprobanteResponseDTO.DetalleDTO.builder()
                                .item(d.getItem())
                                .codigo(d.getCodigo())
                                .descripcion(d.getDescripcion())
                                .unidadMedida(d.getUnidadMedida())
                                .cantidad(d.getCantidad())
                                .valorUnitario(d.getValorUnitario())
                                .precioUnitario(d.getPrecioUnitario())
                                .valorVenta(d.getValorVenta())
                                .igv(d.getIgv())
                                .importeTotal(d.getImporteTotal())
                                .build())
                        .toList())
                .build();
    }
}
