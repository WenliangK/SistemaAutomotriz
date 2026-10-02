package com.autogestion.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Error de regla de negocio explicado para humanos: qué pasó, en qué campo
 * y cómo corregirlo. El frontend lo muestra tal cual, sin tecnicismos.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String campo;
    private final String sugerencia;
    private final HttpStatus estado;

    public BusinessException(String mensaje, String campo, String sugerencia) {
        this(mensaje, campo, sugerencia, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String mensaje, String campo, String sugerencia, HttpStatus estado) {
        super(mensaje);
        this.campo = campo;
        this.sugerencia = sugerencia;
        this.estado = estado;
    }
}
