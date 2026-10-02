package com.autogestion.entity;

public enum MetodoPago {
    EFECTIVO,
    TARJETA,
    YAPE,
    PLIN,
    TRANSFERENCIA;

    public static MetodoPago desde(String codigo) {
        if (codigo == null) return null;
        try {
            return MetodoPago.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
