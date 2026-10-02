# Preguntas de defensa — AutoGestion

Respuestas cortas para decir en voz alta.

1. **¿Por qué no guardar comprobantes en el navegador?**
   Porque se pierden al cambiar de PC o borrar caché, y dos cajeros repetirían el mismo número. Los datos de negocio viven en la BD; el navegador solo guarda preferencias (tema, banners).

2. **¿Qué es serie y correlativo?**
   La matrícula del comprobante: serie `B001`/`F001` + número secuencial de 8 dígitos. Se reserva con bloqueo pesimista en una transacción: dos emisiones a la vez no repiten número.

3. **¿Cómo se valida un RUC?**
   11 dígitos, prefijo 10/15/16/17/20 y dígito verificador (pesos 5-4-3-2-7-6-5-4-3-2 sobre los 10 primeros, módulo 11). El mismo `DocumentoValidator` se usa al emitir y en la validación en vivo.

4. **¿Por qué se guarda una copia de los datos del cliente en el comprobante?**
   Es un snapshot: la factura es una foto de ese día. Si el cliente cambia de dirección mañana, lo impreso no debe cambiar.

5. **¿Diferencia entre boleta y factura?**
   Boleta: consumidor final con DNI, sin crédito fiscal. Factura: empresa con RUC válido + razón social + dirección fiscal, con crédito fiscal. Boleta mayor a S/ 700 exige DNI.

6. **¿Qué falta para ser facturación electrónica real?**
   Firma digital con certificado, XML UBL 2.1, envío a SUNAT/OSE y QR verificable. Por eso el pie dice "representación impresa académica, no válida ante SUNAT".

7. **¿Cómo se calcula el IGV?**
   Los precios incluyen IGV: valor sin IGV = precio / 1.18 por línea; IGV = total − gravado, para que total = gravado + IGV al centavo, todo con `BigDecimal`.

8. **¿Qué es un kardex?**
   La cuenta corriente de un producto: saldo inicial + entradas − salidas = saldo final. Cada movimiento guarda stock antes/después, así cuadra con el estante.

9. **¿Cómo se calcula la utilidad estimada?**
   Ingresos sin IGV (comprobantes emitidos) menos costo de lo consumido menos gastos operativos. Es académica: no incluye deudas ni depreciación.

10. **¿Por qué Flyway y no `ddl-auto=update`?**
    Las migraciones versionadas se aplican solas al arrancar, incluso con datos (baseline), y nunca se editan una vez aplicadas. `update` adivina cambios y rompe producción.

11. **¿Por qué el monto lo calcula el servidor?**
    Porque lo que viene del navegador se puede manipular con DevTools. El total sale de la cotización en una transacción.

12. **¿Qué pasa si anulo un comprobante?**
    Queda ANULADO con motivo (auditoría), su número jamás se reutiliza y la OT puede emitir otro.

13. **¿Cómo evitan fusionar clientes distintos?**
    La identidad es (tipo de documento, documento). Teléfono y email son contacto, nunca identidad.

14. **¿Por qué desactivar en vez de borrar?**
    Borrar rompería el historial (OT, comprobantes, movimientos). Desactivar conserva todo y bloquea el acceso.
