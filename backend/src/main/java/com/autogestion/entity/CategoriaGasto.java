package com.autogestion.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoriaGasto {
    ALQUILER("Alquiler del local"),
    SERVICIOS("Luz, agua, internet"),
    SUELDOS("Sueldos y pagos al equipo"),
    HERRAMIENTAS("Herramientas y equipos"),
    LIMPIEZA("Limpieza e insumos"),
    TRANSPORTE("Transporte y delivery"),
    OTROS("Otros gastos");

    private final String etiqueta;

    public static CategoriaGasto desde(String codigo) {
        if (codigo == null) return null;
        try {
            return CategoriaGasto.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
