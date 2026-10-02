package com.autogestion.entity;

/** Dónde va el vehículo en el flujo. En BD se guarda el nombre (STRING): sin migración. */
public enum EstadoRecepcion {
    PENDIENTE,
    EN_DIAGNOSTICO,
    COTIZADA,
    EN_TRABAJO,
    FINALIZADA,
    ENTREGADA;

    public static EstadoRecepcion desde(String codigo) {
        if (codigo == null) return null;
        try {
            return EstadoRecepcion.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
