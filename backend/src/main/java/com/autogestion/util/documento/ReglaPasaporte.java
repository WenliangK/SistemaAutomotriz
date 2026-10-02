package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.DocumentoValidator;

/** Pasaporte: 6 a 12 letras o números. */
public final class ReglaPasaporte implements ReglaDocumento {

    @Override
    public TipoDocumento tipo() {
        return TipoDocumento.PASAPORTE;
    }

    @Override
    public boolean valido(String pasaporte) {
        return DocumentoValidator.normalizar(pasaporte).matches("[A-Z0-9]{6,12}");
    }

    @Override
    public String mensaje(String numero) {
        return "El pasaporte lleva de 6 a 12 letras o números.";
    }
}
