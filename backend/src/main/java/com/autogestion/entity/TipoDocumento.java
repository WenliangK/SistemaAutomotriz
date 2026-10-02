package com.autogestion.entity;

/** Tipo de documento de identidad (catálogo 06 SUNAT entre paréntesis). */
public enum TipoDocumento {
    DNI,        // 1
    RUC,        // 6
    CE,         // 4
    PASAPORTE;  // 7

    public static TipoDocumento desde(String codigo) {
        if (codigo == null) return null;
        try {
            return TipoDocumento.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
