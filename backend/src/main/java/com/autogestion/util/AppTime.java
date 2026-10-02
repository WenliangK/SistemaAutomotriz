package com.autogestion.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Único reloj del sistema: America/Lima. Todos los "ahora" de negocio
 * (ingresos, pagos, emisiones, movimientos) pasan por aquí para que los
 * reportes por día/mes coincidan sin importar la zona del servidor.
 */
public final class AppTime {

    private AppTime() { }

    public static final ZoneId ZONA = ZoneId.of("America/Lima");

    public static LocalDateTime ahora() {
        return LocalDateTime.now(ZONA);
    }

    public static LocalDate hoy() {
        return LocalDate.now(ZONA);
    }
}
