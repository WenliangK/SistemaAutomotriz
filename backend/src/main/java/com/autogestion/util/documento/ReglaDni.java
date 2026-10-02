package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.DocumentoValidator;

/** DNI peruano: 8 dígitos. */
public final class ReglaDni implements ReglaDocumento {

    @Override
    public TipoDocumento tipo() {
        return TipoDocumento.DNI;
    }

    @Override
    public boolean valido(String dni) {
        return DocumentoValidator.soloDigitos(dni).matches("\\d{8}");
    }

    @Override
    public String mensaje(String numero) {
        String n = DocumentoValidator.normalizar(numero);
        if (n.isEmpty()) return "Escribe los 8 dígitos del DNI.";
        if (n.length() == 11 && n.matches("\\d+"))
            return "Tiene 11 dígitos: eso es un RUC, no un DNI. Cambia el comprobante a FACTURA.";
        if (!n.matches("\\d+"))
            return "El DNI lleva solo números: tienes " + n.length()
                    + " caracteres y hay letras o símbolos (los 8 dígitos, sin puntos).";
        return "El DNI debe tener 8 dígitos numéricos (llevas " + n.length() + ").";
    }
}
