package com.autogestion.entity;

public enum FormaPago {
    CONTADO,
    CREDITO;

    public static FormaPago desde(String codigo) {
        if (codigo == null || codigo.isBlank()) return CONTADO;
        try {
            return FormaPago.valueOf(codigo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
