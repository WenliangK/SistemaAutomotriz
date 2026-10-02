package com.autogestion.entity;

/** Estados de la cotización. En BD se guarda el nombre (STRING): sin migración. */
public enum EstadoCotizacion {
    PENDIENTE,
    EN_DIAGNOSTICO,
    APROBADA,
    RECHAZADA,
    CONVERTIDA;

    public static EstadoCotizacion desde(String codigo) {
        if (codigo == null) return null;
        try {
            return EstadoCotizacion.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
