package com.autogestion.entity;

/**
 * Estados de la orden de trabajo. En BD se guarda el nombre (STRING): sin migración.
 *
 * OCP/SRP: el propio tipo conoce sus transiciones válidas. Agregar un estado
 * nuevo obliga a cubrirlo aquí (el switch es exhaustivo: compila solo si todos
 * los casos devuelven algo) en vez de cazar ifs regados por los servicios.
 */
public enum EstadoOT {
    PENDIENTE,
    EN_PROCESO,
    EN_PRUEBA,
    FINALIZADA,
    CANCELADA;

    public static EstadoOT desde(String codigo) {
        if (codigo == null) return null;
        try {
            return EstadoOT.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** ¿Se puede pasar de este estado al destino siguiendo el flujo del taller? */
    public boolean puedePasarA(EstadoOT destino) {
        if (destino == null) return false;
        return switch (this) {
            case PENDIENTE -> destino == EN_PROCESO || destino == CANCELADA;
            case EN_PROCESO -> destino == EN_PRUEBA || destino == FINALIZADA || destino == CANCELADA;
            case EN_PRUEBA -> destino == FINALIZADA || destino == EN_PROCESO || destino == CANCELADA;
            case FINALIZADA, CANCELADA -> false;
        };
    }

    /** Estados que cierran la OT: ya no aceptan cambios ni reasignaciones. */
    public boolean estaCerrada() {
        return this == FINALIZADA || this == CANCELADA;
    }
}
