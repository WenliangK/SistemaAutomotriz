package com.autogestion.entity;

/**
 * ENTRADA: compra o ingreso (suma stock, guarda costo y proveedor).
 * CONSUMO: repuesto usado en una OT (resta stock, guarda OT y costo del momento).
 * AJUSTE_POSITIVO: sobrante encontrado (suma, con motivo).
 * AJUSTE_NEGATIVO / MERMA: pérdida o vencimiento (restan, motivo obligatorio).
 */
public enum TipoMovimiento {
    ENTRADA,
    CONSUMO,
    AJUSTE_POSITIVO,
    AJUSTE_NEGATIVO,
    MERMA;

    public static TipoMovimiento desde(String codigo) {
        if (codigo == null) return null;
        try {
            return TipoMovimiento.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
