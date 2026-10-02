package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;

import java.util.Map;

/**
 * Registro de reglas por tipo de documento. Para soportar un tipo nuevo:
 * crear su {@link ReglaDocumento} y agregarla a este mapa. Nada más cambia.
 */
public final class ReglasDocumento {

    private static final Map<TipoDocumento, ReglaDocumento> REGLAS = Map.of(
            TipoDocumento.DNI, new ReglaDni(),
            TipoDocumento.RUC, new ReglaRuc(),
            TipoDocumento.CE, new ReglaCe(),
            TipoDocumento.PASAPORTE, new ReglaPasaporte());

    private ReglasDocumento() { }

    /** Regla del tipo pedido (null si el tipo es null o no está registrado). */
    public static ReglaDocumento para(TipoDocumento tipo) {
        if (tipo == null) return null;
        return REGLAS.get(tipo);
    }
}
