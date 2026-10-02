package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.DocumentoValidator;

/** Carnet de extranjería: 9 a 12 letras o números. */
public final class ReglaCe implements ReglaDocumento {

    @Override
    public TipoDocumento tipo() {
        return TipoDocumento.CE;
    }

    @Override
    public boolean valido(String ce) {
        return DocumentoValidator.normalizar(ce).matches("[A-Z0-9]{9,12}");
    }

    @Override
    public String mensaje(String numero) {
        return "El carnet de extranjería lleva de 9 a 12 letras o números.";
    }
}
