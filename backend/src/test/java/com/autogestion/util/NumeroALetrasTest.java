package com.autogestion.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NumeroALetrasTest {

    private String de(String numero) {
        return NumeroALetras.convertir(new BigDecimal(numero));
    }

    @Test
    void vectoresDelEnunciado() {
        assertEquals("SON: CERO Y 00/100 SOLES", de("0"));
        assertEquals("SON: UNO Y 00/100 SOLES", de("1"));
        assertEquals("SON: QUINCE Y 00/100 SOLES", de("15"));
        assertEquals("SON: CIEN Y 00/100 SOLES", de("100"));
        assertEquals("SON: CIENTO UNO Y 00/100 SOLES", de("101"));
        assertEquals("SON: NOVECIENTOS NOVENTA Y NUEVE Y 00/100 SOLES", de("999"));
        assertEquals("SON: MIL Y 00/100 SOLES", de("1000"));
        assertEquals("SON: MIL UNO Y 00/100 SOLES", de("1001"));
        assertEquals("SON: VEINTIUNO MIL Y 00/100 SOLES", de("21000"));
        assertEquals("SON: UN MILLON Y 00/100 SOLES", de("1000000"));
    }

    @Test
    void centavosComoFraccion() {
        assertEquals("SON: MIL DOSCIENTOS TREINTA Y 50/100 SOLES", de("1230.50"));
        assertEquals("SON: SEISCIENTOS TREINTA Y 00/100 SOLES", de("630"));
    }
}
