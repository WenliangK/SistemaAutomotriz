# Changelog

## [1.0.0] - 2026-09-14

### Fixed Bugs

#### Backend

**LazyInitializationException (500 Internal Server Error)**
- **Problem**: Entities with `@ManyToOne(fetch = FetchType.LAZY)` relationships caused `LazyInitializationException` when serializing to JSON outside of transaction scope
- **Solution**: Created DTOs for all entities and modified services to return DTOs instead of entities directly. Added `@Transactional(readOnly = true)` to read methods and mapped entities to DTOs inside transaction boundaries
- **Files Modified**:
  - `DiagnosticoResponseDTO.java` (new)
  - `VehiculoResponseDTO.java` (new)
  - `RecepcionResponseDTO.java` (new)
  - `OrdenTrabajoResponseDTO.java` (new)
  - `OtProductoUsadoResponseDTO.java` (new)
  - `PagoEntregaResponseDTO.java` (new)
  - `CotizacionResponseDTO.java` (updated)
  - `VehiculoResponseDTO.java` (new)
  - `DiagnosticoService.java` - Added `@Transactional` to all public methods, returns DTOs
  - `CotizacionService.java` - Added `@Transactional(readOnly = true)` to read methods, returns DTOs
  - `VehiculoService.java` - Added `@Transactional` to all methods, returns DTOs
  - `RecepcionService.java` - Added `@Transactional` to all methods, returns DTOs
  - `PagoEntregaService.java` - Added `@Transactional(readOnly = true)` to read methods, returns DTOs
  - `OrdenTrabajoService.java` - Added `@Transactional(readOnly = true)` to read methods, returns DTOs
  - All controllers updated to return DTOs instead of entities

**Duplicate Payment Constraint Violation (400)**
- **Problem**: Creating duplicate payment for same OT caused 500 error due to unique constraint violation
- **Solution**: Modified `registrarPagoInterno()` in `OrdenTrabajoService` to check for existing payment and throw clear error message if payment already exists
- **Files Modified**: `OrdenTrabajoService.java`

**Cotización State Management**
- **Problem**: Cotizaciones remained in `APROBADA` state after creating OT, allowing duplicate OT creation
- **Solution**: Added state transition to `CONVERTIDA` after OT creation in both `crear()` and `crearCompleta()` methods
- **Database Migration**: Added `CONVERTIDA` to `cotizacion_estado_check` constraint
- **Files Modified**: `OrdenTrabajoService.java`, database migration

**CotizaciónRepository Query Optimization**
- **Problem**: `findAll()` caused `LazyInitializationException` due to nested LAZY relationships
- **Solution**: Added `LEFT JOIN FETCH` queries for `diagnostico`, `recepcion`, `vehiculo`, `cliente`, `mecanico` relationships
- **Files Modified**: `CotizacionRepository.java`, `VehiculoRepository.java`, `DiagnosticoRepository.java`

**PagoEntregaRepository**
- Added `LEFT JOIN FETCH` for `ordenTrabajo` relationship

**PagoEntregaService** - Added `@Transactional(readOnly = true)` to `obtenerMonto()`

**GlobalExceptionHandler**
- Added `RuntimeException` handler with fallback message "Error inesperado, contacte soporte"

**OrdenTrabajoService**
- Added `OtProductoUsadoResponseDTO` mapping
- Added `@Transactional(readOnly = true)` to `listarFinalizadasConPago()`
- Made `toResponseDTO()` public for cross-service usage

#### Frontend

**Cache Busting**
- Added `?v=20260913b` query parameter to all CSS/JS references in HTML files

**LazyInitializationException in Frontend**
- Fixed field mapping in `pago_entrega.html` and `mecanico.html` to use flat DTO fields instead of nested objects

**Race Condition in `loadOrdenes()`**
- Added sequence guard (`loadOrdenesSeq`) to prevent race conditions
- Removed inline `onchange="loadOrdenes()"` from select element, replaced with `addEventListener`
- Added sequence guard in catch block

**IIFE Scope Issue (ReferenceError)**
- **Problem**: Functions defined inside IIFE were not accessible to inline `onclick` handlers
- **Solution**: Exposed functions to `window` object (`window.aprobarCotizacion`, `window.rechazarCotizacion`, `window.agregarServicio`, `window.agregarProducto`, `window.eliminarItem`)

**Custom Confirm Modal**
- Created `frontend/js/confirm-modal.js` with reusable `confirmarAccion(mensaje, opciones)` function
- Returns `Promise<boolean>` for async/await usage
- Replaced all native `confirm()` calls in `orden_trabajo.html` and `pago_entrega.html`
- Reused existing `.ag-modal-overlay` / `.ag-modal` CSS styles

**Cache Busting for Static Assets**
- Added `?v=20260913b` query parameter to all CSS/JS references in all HTML pages

**Select Refresh After Submit**
- `orden_trabajo.html`: Re-populates `cotizacionSelect` after OT creation
- `cotizacion.html`: Re-populates `recepcionSelect` after diagnóstico creation
- Removed stale cached options that caused duplicate submissions

**OtProductoUsado Endpoints**
- Created `OtProductoUsadoResponseDTO`
- Modified `registrarProductoUsado()` and `listarProductosUsados()` to return DTOs
- Added `toProductoUsadoResponseDTO()` helper method

**GlobalExceptionHandler**
- Added `RuntimeException` handler with fallback message "Error inesperado, contacte soporte"
- Proper handler ordering (specific first, generic last)

#### Database
- Added `CONVERTIDA` to `cotizacion_estado_check` constraint
- Added `LEFT JOIN FETCH` to `PagoEntregaRepository.findByOrdenTrabajoId()`
- Added `LEFT JOIN FETCH` to `OtProductoUsadoRepository.findByOrdenTrabajoId()`

### Known Issues / Future Improvements
- Consider creating specific exception classes (`NotFoundException`, `ConflictException`) for more precise HTTP status codes (404, 409)
- Current implementation returns 400 for all RuntimeExceptions

## [Unreleased]
- Remove all code comments (`//`, `--`, `/** */`) from Java, JS, HTML, CSS files
</content>