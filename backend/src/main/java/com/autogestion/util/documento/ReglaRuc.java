package com.autogestion.util.documento;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.DocumentoValidator;

import java.util.Set;

/** RUC SUNAT: 11 dígitos, prefijo válido y dígito verificador correcto. */
public final class ReglaRuc implements ReglaDocumento {

    private static final int[] PESOS = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final Set<String> PREFIJOS = Set.of("10", "15", "16", "17", "20");

    @Override
    public TipoDocumento tipo() {
        return TipoDocumento.RUC;
    }

    @Override
    public boolean valido(String ruc) {
        String n = DocumentoValidator.soloDigitos(ruc);
        if (!n.matches("\\d{11}")) return false;
        if (!PREFIJOS.contains(n.substring(0, 2))) return false;
        return digitoVerificador(n) == (n.charAt(10) - '0');
    }

    /**
     * Dígito verificador que DEBERÍA tener el RUC (-1 si no se puede calcular).
     * Sirve para decirle al usuario "termina en 9 pero debería terminar en 4".
     */
    public static int digitoVerificador(String ruc) {
        String n = DocumentoValidator.normalizar(ruc);
        if (!n.matches("\\d{10,11}")) return -1;
        int suma = 0;
        for (int i = 0; i < 10; i++) suma += (n.charAt(i) - '0') * PESOS[i];
        int dv = 11 - (suma % 11);
        if (dv == 10) dv = 0;
        else if (dv == 11) dv = 1;
        return dv;
    }

    @Override
    public String mensaje(String numero) {
        String n = DocumentoValidator.normalizar(numero);
        if (!n.matches("\\d+")) return "El RUC solo lleva dígitos, sin puntos ni guiones.";
        if (n.length() != 11) {
            if (n.length() == 8) return "Tiene 8 dígitos: eso es un DNI, no un RUC. Cambia el comprobante a BOLETA.";
            return "El RUC debe tener 11 dígitos (llevas " + n.length() + ").";
        }
        if (!PREFIJOS.contains(n.substring(0, 2))) return "El RUC debe empezar con 10, 15, 16, 17 o 20.";
        int dv = digitoVerificador(n);
        return "RUC inválido: el último dígito es de control y no coincide."
                + (dv >= 0 ? " Con esos 10 primeros dígitos debería terminar en " + dv + "." : "")
                + " Revisa los números.";
    }
}
