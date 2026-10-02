package com.autogestion.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Especialidades del taller (se muestran con etiqueta legible en la interfaz). */
@Getter
@RequiredArgsConstructor
public enum Especialidad {
    MECANICA_GENERAL("Mecánica general"),
    MOTOR("Motor"),
    FRENOS_SUSPENSION("Frenos y suspensión"),
    ELECTRICIDAD("Electricidad"),
    DIAGNOSTICO_COMPUTARIZADO("Diagnóstico computarizado"),
    ALINEACION_BALANCEO("Alineación y balanceo");

    private final String etiqueta;

    public static Especialidad desde(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;
        try {
            return Especialidad.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
