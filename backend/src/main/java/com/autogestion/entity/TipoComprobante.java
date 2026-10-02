package com.autogestion.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Comprobante de pago: código SUNAT + serie asignada. */
@Getter
@RequiredArgsConstructor
public enum TipoComprobante {
    BOLETA("03", "B001"),
    FACTURA("01", "F001");

    private final String codigoSunat;
    private final String serie;

    public static TipoComprobante desde(String codigo) {
        if (codigo == null) return null;
        try {
            return TipoComprobante.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
