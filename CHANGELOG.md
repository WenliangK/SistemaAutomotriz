# Changelog

## [1.0.0] - 2026-09-14

### Corrección de errores

#### Backend

**LazyInitializationException (Error 500)**
- **Problema:** Las entidades con relaciones `@ManyToOne(fetch = FetchType.LAZY)` provocaban `LazyInitializationException` al serializar la respuesta a JSON fuera del alcance de una transacción.
- **Solución:** Se crearon DTOs para las entidades y se modificaron los servicios para devolver DTOs en lugar de entidades directamente. También se agregó `@Transactional(readOnly = true)` a los métodos de lectura y se realizó el mapeo de las entidades a DTOs dentro de los límites transaccionales.
- **Archivos modificados:**
  - `DiagnosticoResponseDTO.java` (nuevo)
  - `VehiculoResponseDTO.java` (nuevo)
  - `RecepcionResponseDTO.java` (nuevo)
  - `OrdenTrabajoResponseDTO.java` (nuevo)
  - `OtProductoUsadoResponseDTO.java` (nuevo)
  - `PagoEntregaResponseDTO.java` (nuevo)
  - `CotizacionResponseDTO.java` (actualizado)
  - `DiagnosticoService.java` - Se agregó `@Transactional` a los métodos públicos y ahora devuelve DTOs.
  - `CotizacionService.java` - Se agregó `@Transactional(readOnly = true)` a los métodos de lectura y ahora devuelve DTOs.
  - `VehiculoService.java` - Se agregó `@Transactional` a los métodos y ahora devuelve DTOs.
  - `RecepcionService.java` - Se agregó `@Transactional` a los métodos y ahora devuelve DTOs.
  - `PagoEntregaService.java` - Se agregó `@Transactional(readOnly = true)` a los métodos de lectura y ahora devuelve DTOs.
  - `OrdenTrabajoService.java` - Se agregó `@Transactional(readOnly = true)` a los métodos de lectura y ahora devuelve DTOs.
  - Todos los controladores fueron actualizados para devolver DTOs en lugar de entidades.

**Violación de restricción por pago duplicado (400)**
- **Problema:** Registrar un pago duplicado para la misma OT provocaba un error 500 debido a la violación de una restricción única.
- **Solución:** Se modificó `registrarPagoInterno()` en `OrdenTrabajoService` para comprobar si ya existe un pago y mostrar un mensaje de error claro cuando se intenta registrar uno duplicado.
- **Archivo modificado:** `OrdenTrabajoService.java`

**Gestión del estado de las cotizaciones**
- **Problema:** Las cotizaciones permanecían en estado `APROBADA` después de crear una OT, permitiendo crear múltiples OT a partir de la misma cotización.
- **Solución:** Se agregó la transición de estado a `CONVERTIDA` después de crear la OT, tanto en `crear()` como en `crearCompleta()`.
- **Migración de base de datos:** Se agregó `CONVERTIDA` a la restricción `cotizacion_estado_check`.
- **Archivos modificados:** `OrdenTrabajoService.java` y migración de base de datos.

**Optimización de consultas en `CotizacionRepository`**
- **Problema:** `findAll()` provocaba `LazyInitializationException` debido a relaciones `LAZY` anidadas.
- **Solución:** Se agregaron consultas con `LEFT JOIN FETCH` para las relaciones `diagnostico`, `recepcion`, `vehiculo`, `cliente` y `mecanico`.
- **Archivos modificados:** `CotizacionRepository.java`, `VehiculoRepository.java`, `DiagnosticoRepository.java`

**PagoEntregaRepository**
- Se agregó `LEFT JOIN FETCH` para la relación `ordenTrabajo`.

**PagoEntregaService**
- Se agregó `@Transactional(readOnly = true)` al método `obtenerMonto()`.

**GlobalExceptionHandler**
- Se agregó un manejador para `RuntimeException` con el mensaje de respaldo: `"Error inesperado, contacte soporte"`.

**OrdenTrabajoService**
- Se agregó el mapeo mediante `OtProductoUsadoResponseDTO`.
- Se agregó `@Transactional(readOnly = true)` a `listarFinalizadasConPago()`.
- Se hizo público `toResponseDTO()` para permitir su uso desde otros servicios.

#### Frontend

**Cache Busting**
- Se agregó el parámetro `?v=20260913b` a las referencias de CSS y JS en los archivos HTML.

**LazyInitializationException en el frontend**
- Se corrigió el mapeo de campos en `pago_entrega.html` y `mecanico.html` para utilizar los campos planos de los DTOs en lugar de objetos anidados.

**Race Condition en `loadOrdenes()`**
- Se agregó un control de secuencia mediante `loadOrdenesSeq` para evitar condiciones de carrera.
- Se eliminó `onchange="loadOrdenes()"` del elemento `select` y se reemplazó por `addEventListener`.
- Se agregó el control de secuencia también en el bloque `catch`.

**Problema de alcance con IIFE (ReferenceError)**
- **Problema:** Las funciones definidas dentro de una IIFE no eran accesibles desde los manejadores `onclick` escritos directamente en HTML.
- **Solución:** Se expusieron las funciones necesarias mediante el objeto `window`:
  - `window.aprobarCotizacion`
  - `window.rechazarCotizacion`
  - `window.agregarServicio`
  - `window.agregarProducto`
  - `window.eliminarItem`

**Modal de confirmación personalizado**
- Se creó `frontend/js/confirm-modal.js` con la función reutilizable `confirmarAccion(mensaje, opciones)`.
- La función devuelve `Promise<boolean>` para permitir su uso con `async/await`.
- Se reemplazaron todas las llamadas a `confirm()` nativo en `orden_trabajo.html` y `pago_entrega.html`.
- Se reutilizaron los estilos CSS existentes `.ag-modal-overlay` y `.ag-modal`.

**Cache Busting para recursos estáticos**
- Se agregó el parámetro `?v=20260913b` a las referencias de CSS y JS en todas las páginas HTML.

**Actualización de selects después del envío**
- `orden_trabajo.html`: Se vuelve a cargar `cotizacionSelect` después de crear una OT.
- `cotizacion.html`: Se vuelve a cargar `recepcionSelect` después de crear un diagnóstico.
- Se eliminaron opciones almacenadas en caché que podían provocar envíos duplicados.

**Endpoints de `OtProductoUsado`**
- Se creó `OtProductoUsadoResponseDTO`.
- Se modificaron `registrarProductoUsado()` y `listarProductosUsados()` para devolver DTOs.
- Se agregó el método auxiliar `toProductoUsadoResponseDTO()`.

**GlobalExceptionHandler**
- Se agregó un manejador para `RuntimeException` con el mensaje de respaldo: `"Error inesperado, contacte soporte"`.
- Se estableció el orden correcto de los manejadores, colocando los específicos antes del genérico.

#### Base de datos

- Se agregó `CONVERTIDA` a la restricción `cotizacion_estado_check`.
- Se agregó `LEFT JOIN FETCH` a `PagoEntregaRepository.findByOrdenTrabajoId()`.
- Se agregó `LEFT JOIN FETCH` a `OtProductoUsadoRepository.findByOrdenTrabajoId()`.

### Problemas conocidos / Mejoras futuras

- Considerar la creación de excepciones específicas como `NotFoundException` y `ConflictException` para utilizar códigos HTTP más precisos, como 404 y 409.
- Actualmente, todas las `RuntimeException` se responden con código HTTP 400.
