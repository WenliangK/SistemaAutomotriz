package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;

/**
 * OCP: cada tipo de documento encapsula su propia regla de validación
 * y su mensaje en lenguaje claro. Un tipo nuevo = una clase nueva que
 * implementa esta interfaz y una línea en {@link ReglasDocumento}:
 * ningún {@code switch} existente se toca.
 */
public interface ReglaDocumento {

    /** Tipo que atiende esta regla. */
    TipoDocumento tipo();

    /** ¿El número cumple la regla? Acepta null y suciedad (espacios, guiones). */
    boolean valido(String numero);

    /** Mensaje en lenguaje claro para mostrar bajo el campo del formulario. */
    String mensaje(String numero);
}
