package com.autogestion.util;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.documento.ReglaDocumento;
import com.autogestion.util.documento.ReglaRuc;
import com.autogestion.util.documento.ReglasDocumento;

/**
 * Fuente de verdad para validar documentos (backend). El frontend replica
 * estas mismas reglas solo para UX; lo que vale es lo que dice esta clase.
 * Ver columna "Algoritmo del dígito verificador" en el enunciado Fase 1.
 *
 * OCP: la lógica vive en {@link ReglaDocumento} (una clase por tipo,
 * registradas en {@link ReglasDocumento}). Esta clase es solo la fachada
 * con el API que ya usan servicios, DTOs, controladores y tests: ningún
 * llamador cambió.
 */
public final class DocumentoValidator {

    private DocumentoValidator() { }

    /** Limpia espacios/guiones y mayúsculas (CE y pasaporte). Nunca devuelve null. */
    public static String normalizar(String numero) {
        if (numero == null) return "";
        return numero.trim().replaceAll("[\\s\\-]", "").toUpperCase();
    }

    /** Limpia todo lo que no sea dígito (para validación de DNI/RUC). */
    public static String soloDigitos(String numero) {
        if (numero == null) return "";
        return numero.replaceAll("\\D", "");
    }

    public static boolean dniValido(String dni) {
        return ReglasDocumento.para(TipoDocumento.DNI).valido(dni);
    }

    public static boolean rucValido(String ruc) {
        return ReglasDocumento.para(TipoDocumento.RUC).valido(ruc);
    }

    /**
     * Dígito verificador que DEBERÍA tener el RUC (-1 si no se puede calcular).
     * Sirve para decirle al usuario "termina en 9 pero debería terminar en 4".
     */
    public static int digitoVerificadorRuc(String ruc) {
        return ReglaRuc.digitoVerificador(ruc);
    }

    public static boolean ceValido(String ce) {
        return ReglasDocumento.para(TipoDocumento.CE).valido(ce);
    }

    public static boolean pasaporteValido(String pas) {
        return ReglasDocumento.para(TipoDocumento.PASAPORTE).valido(pas);
    }

    public static boolean valido(TipoDocumento tipo, String numero) {
        ReglaDocumento regla = ReglasDocumento.para(tipo);
        return regla != null && regla.valido(numero);
    }

    /** Mensaje en lenguaje claro para mostrar bajo el campo del formulario. */
    public static String mensajeError(TipoDocumento tipo, String numero) {
        ReglaDocumento regla = ReglasDocumento.para(tipo);
        if (regla == null) return "Elige el tipo de documento primero.";
        return regla.mensaje(numero);
    }
}
