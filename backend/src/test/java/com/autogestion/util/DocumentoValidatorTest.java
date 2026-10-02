package com.autogestion.util;

import com.autogestion.entity.TipoDocumento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentoValidatorTest {

    @Test
    void rucsValidos() {
        assertTrue(DocumentoValidator.rucValido("20123456786")); // jurídica
        assertTrue(DocumentoValidator.rucValido("20600000013")); // jurídica
        assertTrue(DocumentoValidator.rucValido("10123456781")); // persona natural con negocio
        assertTrue(DocumentoValidator.valido(TipoDocumento.RUC, " 20123456786 ")); // con espacios
    }

    @Test
    void rucDigitoVerificadorIncorrecto() {
        assertFalse(DocumentoValidator.rucValido("20123456785")); // debe fallar: dv real es 6
    }

    @Test
    void rucPrefijoInvalido() {
        assertFalse(DocumentoValidator.rucValido("30123456786")); // 30 no es prefijo SUNAT
    }

    @Test
    void rucLongitudYFormato() {
        assertFalse(DocumentoValidator.rucValido("2012345678"));   // 10 dígitos
        assertFalse(DocumentoValidator.rucValido("201234567861")); // 12 dígitos
        assertFalse(DocumentoValidator.rucValido("2012345678A")); // letra
        assertFalse(DocumentoValidator.rucValido(null));
        assertFalse(DocumentoValidator.rucValido(""));
    }

    @Test
    void dni() {
        assertTrue(DocumentoValidator.dniValido("45123698"));
        assertTrue(DocumentoValidator.valido(TipoDocumento.DNI, "45123698"));
        assertFalse(DocumentoValidator.dniValido("4512369"));   // 7 dígitos
        assertFalse(DocumentoValidator.dniValido("451236987")); // 9 dígitos
        assertFalse(DocumentoValidator.dniValido("4512369A"));
    }

    @Test
    void ceYPasaporte() {
        assertTrue(DocumentoValidator.ceValido("001234567"));
        assertTrue(DocumentoValidator.ceValido("ABC123456789"));
        assertFalse(DocumentoValidator.ceValido("12345678")); // 8, muy corto
        assertTrue(DocumentoValidator.pasaporteValido("AB123456"));
        assertFalse(DocumentoValidator.pasaporteValido("AB12")); // 4, muy corto
    }

    @Test
    void digitoEsperadoDelataErroresDeTipeo() {
        assertEquals(6, DocumentoValidator.digitoVerificadorRuc("20123456785"));
        assertEquals(4, DocumentoValidator.digitoVerificadorRuc("20707162819"));
        assertFalse(DocumentoValidator.rucValido("20707162819")); // termina en 9, debería en 4
        assertEquals(-1, DocumentoValidator.digitoVerificadorRuc("123"));
        assertTrue(DocumentoValidator.mensajeError(TipoDocumento.RUC, "20707162819").contains("terminar en 4"));
    }

    @Test
    void mensajeDeErrorAyudaAlUsuario() {
        String msg = DocumentoValidator.mensajeError(TipoDocumento.RUC, "20123456785");
        assertTrue(msg.contains("control"), "Debe mencionar el dígito de control: " + msg);
        assertTrue(DocumentoValidator.mensajeError(TipoDocumento.RUC, "30123456786").contains("10"));
    }
}
