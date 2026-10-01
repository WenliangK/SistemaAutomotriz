<div align="center">

# 📒 Changelog — AutoGestion

**Historial de cambios del sistema, en lenguaje humano.**
<br>
Lo nuevo arriba, lo viejo abajo. Sin humo: lo pendiente se marca ⏳.

<br>

<img src="https://img.shields.io/badge/version-1.3.0-orange?style=for-the-badge" />
<img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />

</div>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🗂️ Versiones

| Version | Fecha | Lo destacado |
| :-- | :-- | :-- |
| [1.4.1](#-141---2026-10-01) | 2026-10-01 | 🎫 Impresion fiel a pantalla + descarga de constancia |
| [1.4.0](#-140---2026-10-01) | 2026-10-01 | ✨ Rediseño visual guiado · wizard de pago · ticket pro · responsive real |
| [1.3.1](#-131---2026-10-01) | 2026-10-01 | 🐛 Fix crash boleta/factura · paneles exclusivos por rol |
| [1.3.0](#-130---2026-10-01) | 2026-10-01 | 🧾 Boleta/Factura al pagar + constancia imprimible · 📁 Reorden a la raiz |
| [1.2.0](#-120---2026-09-20) | 2026-09-20 | 🛡️ Permisos por rol + pagina 403 + stepper propio |
| [1.1.0](#-110---2026-09-20) | 2026-09-20 | ✨ Rediseño visual completo + tutorial + tour guiado |
| [1.0.0](#-100---2026-09-14) | 2026-09-14 | 🐛 Correccion de errores fundacionales (DTOs, pagos, cache) |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔵 1.4.1 — 2026-10-01

<img src="./readme-assets/ver-141.svg" width="360" alt="v1.4.1 impresión fiel y descarga" />

### 🎫 Lo impreso sale igual que en pantalla + se puede descargar

- **Problema:** al imprimir, la constancia salia sin los colores (cabecera oscura en blanco). Causa: los navegadores quitan los fondos al imprimir por defecto.
- **Fix impresion fiel:** `print-color-adjust: exact` en el ticket y su contenido (`polish.css` + reglas `@media print` de la pagina), regla `@page` con margen de 8mm y bloques que no se cortan a mitad (`break-inside: avoid`).
- **Boton Descargar:** la constancia suma **Cerrar / Descargar / Imprimir**. Descargar baja un archivo `BOLETA-B001-000001.html` autocontenido (estilos en linea, sin internet) que se ve identico y sirve para enviar por WhatsApp/correo o guardar como PDF desde el navegador.
- Sintaxis verificada con Node y llaves CSS balanceadas.

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🟠 1.4.0 — 2026-10-01

<img src="./readme-assets/ver-140.svg" width="330" alt="v1.4.0 rediseño visual guiado" />

### ✨ Todo se ve mejor y se entiende solo

Nueva capa `frontend/css/polish.css` (cargada en las 10 paginas despues de `redesign.css`): solo suma, no rompe el diseño existente.

| Eje | Que cambio |
| :-- | :-- |
| 🧭 Guiado | Banner contextual en cada pagina ("que hago aqui" en 3 pasos + boton al siguiente paso). Se oculta con la X y recuerda tu eleccion. El CTA respeta tu rol (nunca te manda a una pagina prohibida) |
| 📐 Simetria | Nuevo componente `.ag-head` (icono + titulo + meta alineados) aplicado a Pago y Entrega; modales con header/body/footer reales (esas clases existian en el HTML pero sin CSS) |
| 📱 Responsive | Grillas de 4 → 2 columnas en tablet; sidebar apilado <1100px; **navbar movil con scroll horizontal** (antes el menu desaparecia y no habia como navegar); modales tipo hoja inferior; tablas ya tenian scroll |
| ⚡ Dinamismo | Resumen del dia con conteo animado, badges de estado, hovers con elevacion, foco visible para teclado y respeto a `prefers-reduced-motion` |
| 🧾 Wizard de pago | Cobrar ahora es Paso 1 Comprobante (tarjetas Boleta/Factura seleccionables) → Paso 2 Cliente y pago (errores en linea, sin popups) → Paso 3 Confirmar (revision + Confirmar pago). Vista previa del ticket siempre visible al costado |
| 🎫 Ticket nuevo | Constancia profesional: cabecera oscura con marca + folio + fecha, bloque cliente, concepto, Subtotal/IGV/Total destacados y sello PAGO REGISTRADO. Impresion limpia solo-ticket |
| 🧹 Detalles | Dashboard suma `ui-kit.js` (toasts, Ctrl+K, guia); estado vacio "Todo cobrado y entregado" con CTA; `headTotal` con lo por cobrar en el encabezado |

> Sintaxis JS verificada con Node (`ui-kit.js` + wizard de pago) y llaves CSS balanceadas.

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔴 1.3.1 — 2026-10-01

<img src="./readme-assets/ver-131.svg" width="300" alt="v1.3.1 fix de pago y paneles por rol" />

### 🐛 Fix crash al emitir boleta/factura

- **Error:** `can't access property "otId", pagoActual is null` al confirmar el pago. Causa: `confirmarPago()` en `pago_entrega.html` llamaba a `cerrarPagoModal()` (que pone `pagoActual` en `null`) y despues intentaba leer `pagoActual.otId` para mostrar la constancia.
- **Fix:** se capturan `otId` y `monto` en variables locales antes de cerrar el modal; la constancia usa esas copias. Sintaxis verificada con Node.

### 🧍 Paneles exclusivos por rol + rediseño

| Cambio | Detalle |
| :-- | :-- |
| Admin limpio | `ROLE_NAV` del ADMIN ya no incluye `mecanico` ni `recepcionista`; `ROLE_PAGES` deja esos paneles solo para `MECANICO` y `RECEPCIONISTA`. Si el admin entra por URL directa, ve el 403 |
| Panel Mecanico | Header con badge `MECANICO` + saludo personal (`Hola, {nombre}`); accesos rapidos corregidos a sus paginas reales (Mis Ordenes, Inventario, Tutorial). Antes enlazaba a Recepcion y Cotizacion, que le devolvian 403 |
| Panel Recepcionista | Header con badge `RECEPCIONISTA` + saludo personal; accesos rapidos corregidos (Nueva Recepcion, Nueva Cotizacion, Ordenes de Trabajo, Tutorial). Antes enlazaba a Pagos y Entregas, que le devolvia 403 |
| Bypass eliminado | Ambos paneles quitaron el chequeo extra que dejaba pasar al ADMIN; `guardPage()` es la unica puerta |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🟢 1.3.0 — 2026-10-01

<img src="./readme-assets/ver-130.svg" width="520" alt="v1.3.0 actual, frontend hecho, backend pendiente" />

### 🧾 Boleta / Factura al pagar (con constancia para el usuario)

Pagar ya no es un boton a ciegas: ahora eliges el comprobante y te llevas tu constancia impresa.

| Paso | Que pasa |
| :-- | :-- |
| 1 | Clic en **Registrar Pago** → se abre el modal |
| 2 | Eliges **🧾 Boleta** (DNI 8 + nombre) o **🏢 Factura** (RUC 11 + razon social + direccion fiscal) |
| 3 | Eliges metodo: EFECTIVO · YAPE · PLIN · TRANSFERENCIA · TARJETA |
| 4 | Ves el desglose auto: Subtotal + IGV 18% + Total |
| 5 | Confirmas → se registra el pago y se abre tu **constancia** (serie `B001`/`F001` + correlativo) |
| 6 | La tarjeta muestra el badge `BOLETA B001-000123` y el boton **Ver comprobante** reimprime cuando quieras |

> [!NOTE]
> La constancia se guarda en el navegador (`localStorage`) y se imprime con el boton **Imprimir**. Cierra con X, clic fuera o Esc.

> [!WARNING]
> Comprobante **interno imprimible**, NO facturacion electronica SUNAT (sin PSE/OSE, QR ni envio a SUNAT). El comprobante vive en el navegador donde se emitio hasta que el backend lo persista (ver pendiente ⏳ abajo).

<details open>
<summary><b>🔧 Detalle tecnico v1.3.0</b></summary>

<br>

**Modal de pago (`frontend/pages/pago_entrega.html`):**

- `abrirPagoModal()` con radio BOLETA/FACTURA, documento validado por tipo, select de metodo y desglose auto Subtotal (`total/1.18`) + IGV 18% + Total con vista previa de serie.
- Al confirmar: POST existente (`/pago-entrega/completa`) + correlativo por serie en `localStorage` (`ag_comprobantes_v1` / `ag_correlativos_v1`) + ticket con serie-numero, fecha, DNI/RUC, cliente, metodo y totales (`window.print()` con `@media print` solo-ticket).
- Pagos anteriores a v1.3.0 muestran constancia generica reimprimible. Paso 2 del Flujo de Pago renombrado a "Pago + Comprobante".

**⏳ Pendiente backend (gap real):**

- `pago_entrega` aun solo guarda `monto` + fechas (`entity/PagoEntrega.java`, `service/PagoEntregaService.java`, `database/init.sql`).
- Falta migracion `2026-10-01_comprobantes.sql` (`cliente`: `tipo_documento`, `razon_social`, `direccion_fiscal`; `pago_entrega`: `metodo_pago`, `tipo_comprobante`, `serie`, `numero` + `UNIQUE(serie, numero)`, `subtotal`, `igv`, `total`, snapshots, `estado_comprobante`) y payload extendido en `PagoRequest` + `GET /api/comprobantes/{id}`.

</details>

### 📁 Reorden de carpetas a la raiz + rutas corregidas

| Cambio | Detalle |
| :-- | :-- |
| Estructura plana | El codigo subio de `sistema-de-autogestion/sistema-de-autogestion/` a la raiz (`backend/`, `frontend/`, `database/`, `README.md`, `CHANGELOG.md`, `docker-compose.yml`, `readme-assets/`) |
| README | Arbol reescrito (incluye `redesign.css`, `ui-kit.js`, `confirm-modal.js`, `icons.js`, `migrations/`, paneles y `acceso_denegado.html`); comando `docker compose up --build` sin `cd`; puerto DB `5433 (host) → 5432 (contenedor)` |
| Cache-busting | `orden_trabajo.html` y `pago_entrega.html` cargaban `styles.css?v=20260920b?v=20260913b` (doble `?v=`); unificado a `?v=20261001` |
| Navbar (`frontend/js/api.js`) | Nuevo `agIsInPages()` / `agLoginPath()`; `logout()` y `checkAuth()` ya no asumen `../index.html`; el logo ya no apunta a `pages/pages/dashboard.html` |
| `database/Dockerfile` | Ademas de `init.sql` copia `migrations/*.sql` al entrypoint de Postgres |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🟡 1.2.0 — 2026-09-20

<img src="./readme-assets/ver-120.svg" width="440" alt="v1.2.0 roles, seguridad backend, UI stepper" />

> Cada usuario ve solo sus paneles, el backend tambien valida roles y los numeritos ya tienen botones propios.

<details open>
<summary><b>🛡️ Control de acceso por rol (cada usuario ve solo sus paneles)</b></summary>

<br>

- **Mapa centralizado de permisos en `js/api.js`:** nuevo `ROLE_PAGES` (paginas permitidas por rol) y `ROLE_NAV` (links del navbar por rol), con los helpers `guardPage()` (valida sesion + rol y redirige al panel propio), `canSee()` y `homePageForRol()`.
- **Navbar por rol:** cada usuario solo ve los enlaces de sus modulos; el logo lleva a la pagina de inicio de su rol.
- **Login con redireccion por rol:** Admin → Dashboard, Mecanico → Panel Mecanico, Recepcionista → Panel Recepcionista, Almacenero → Inventario.
- **Guard de rol en las 8 paginas** (dashboard, recepcion, cotizacion, orden_trabajo, inventario, pago_entrega, mecanico, recepcionista): quien entra por URL a una pagina no permitida es devuelto a su panel de inicio. Antes solo mecanico y recepcionista validaban rol, y varias paginas no validaban nada.
- **Acciones sensibles ocultas por rol:**
  - Crear ordenes de trabajo: solo ADMIN y RECEPCIONISTA.
  - Registrar pago y entrega del vehiculo: solo ADMIN.
  - Inventario: escritura (nuevo producto, entradas, ajustes de stock) solo para ALMACENERO y ADMIN; MECANICO ve el catalogo en solo lectura.
  - El ADMIN ahora puede abrir los paneles de mecanico y recepcionista (se relajo su validacion interna, que expulsaba a quien no fuera el dueño del panel).
- **Buscador global Ctrl+K y Tutorial:** filtran sus modulos y tarjetas segun el rol.
- **CSS:** regla global `[hidden] { display: none !important; }` porque `.ag-action-link` usaba `display:flex` y anulaba el atributo `hidden` al ocultar acciones por rol.

</details>

<details>
<summary><b>🔒 Seguridad en el backend (roles por endpoint)</b></summary>

<br>

- **`SecurityConfig`:** todas las rutas `/api/**` ahora exigen autenticacion (`authenticated()`); solo `/api/auth/**` (login), la consola H2 y los preflight OPTIONS quedan abiertos. Punto de entrada que responde **401** para peticiones sin token.
- **`@RolesAllowed` en los controladores:** los endpoints validan el rol ademas de la UI:
  - Clientes, Vehiculos, Recepciones, Diagnosticos, Cotizaciones y Ordenes de trabajo (lectura): ADMIN / RECEPCIONISTA / MECANICO.
  - Crear OT (`POST /ordenes-trabajo` y `/completa`): solo ADMIN / RECEPCIONISTA.
  - Cambiar estado de OT y registrar productos usados: solo ADMIN / MECANICO.
  - Inventario: lectura para ADMIN / ALMACENERO / MECANICO; escritura (crear producto, movimientos, actualizar) solo ADMIN / ALMACENERO.
  - Pagos y entregas (pagos, entregas, pago-entrega/completa, finalizadas-completa, monto, consulta de pago): solo ADMIN.
  - Reportes/indicadores: ADMIN / ALMACENERO. Servicios (catalogo): todos los roles.
  - `AuthController` (login) queda abierto por diseño. Se eliminaron los `@PermitAll` sueltos y el `@PreAuthorize("permitAll()")` que dejaban rutas sin proteccion.
- **`GlobalExceptionHandler`:** nuevo handler de `AccessDeniedException` que responde **403** con mensaje claro (antes caeria en el handler de `RuntimeException` y saldria como 400).
- **`apiFetch` distingue 401/403:** un 401 cierra sesion y manda al login; un 403 solo informa el error de permisos (ya no expulsa al usuario de su sesion).
- **Nueva pagina `pages/acceso_denegado.html`:** pantalla "Error 403 / Acceso denegado" con el rol actual y botones "Ir a mi panel" (dinamico segun rol) y "Cerrar sesion". La usan `guardPage()`, los paneles de mecanico/recepcionista y la validacion interna del tutorial; sin sesion activa, la propia pagina devuelve al login.
- **Verificacion:** build del backend OK (Maven dentro de Docker) y sintaxis JS verificada con Node.

</details>

<details>
<summary><b>🛠️ Correcciones de UI y base de datos</b></summary>

<br>

- **Chips de filtro amontonados (Recepcion, Ordenes):** `.ag-section-header` no envolvia contenido; con titulo + 6 chips, los chips se apilaban en columna y el titulo quedaba suelto. Ahora el header hace `flex-wrap` con `gap`.
- **Listas que "aparecian y desaparecian":** `.ag-state-card` tenia `opacity: 0` como estilo base con la animacion `agFadeUp` en modo `backwards` (sin `forwards`); al terminar la animacion, la tarjeta volvia a `opacity: 0` y parecia ocultarse. Corregido a `forwards` (afecta a recepciones, cotizaciones existentes, ordenes y pagos).
- **Error al crear OT (`cotizacion_estado_check`):** `init.sql` nunca recibio los estados nuevos de cotizacion (`EN_DIAGNOSTICO`, `CONVERTIDA`); como el backend corre con `ddl-auto=validate`, la constraint viva de PostgreSQL rechazaba el `UPDATE` a `CONVERTIDA` con un error 400. `init.sql` corregido para instalaciones nuevas y se agrego `database/migrations/2026-09-20_cotizacion_estados.sql` para volumenes de datos existentes.
- **Verificado end-to-end** con el stack levantado en Docker: recepcion → diagnostico → cotizacion → aprobar → crear OT OK (la cotizacion queda `CONVERTIDA`), un almacenero recibe 403 al intentar crear OT y una peticion sin token recibe 401.

</details>

<details>
<summary><b>🔢 UI: stepper de cantidad propio</b></summary>

<br>

- **Fuera spinners nativos:** se ocultaron las flechas nativas de los `input[type=number]` (se veian distintas y feas segun el navegador).
- **Nuevo componente `.ag-stepper`** ( - / input / + ) en el sistema de diseño: botones propios con hover/active/disabled, fondo de foco en el input, soporte de tema oscuro y tres tamanos (`sm`, base, `lg`). Helper `agStepper(id, { value, min, max, step, size })` en `api.js`; los botones respetan min/max/step y disparan eventos `input`/`change`.
- **Aplicado en:** Cantidad Usada del modal de productos usados (Ordenes de trabajo), Cantidad del formulario de movimientos (Inventario) y cantidades de la tabla de productos seleccionables (Cotizacion).
- **Cache busting:** `?v=20260920b` en las referencias a `api.js` y `styles.css` de todas las paginas (dos paginas tenian version vieja y el resto ninguna).

</details>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔵 1.1.0 — 2026-09-20

<img src="./readme-assets/ver-110.svg" width="330" alt="v1.1.0 rediseño, tutorial y tour" />

> El sistema se viste de gala: nueva capa visual, tutorial interactivo y tour guiado.

<details>
<summary><b>✨ Rediseño visual completo del frontend (clic para ver todo lo que cambio)</b></summary>

<br>

- **Nueva capa de diseño (`css/redesign.css`):** fondo con gradientes ambientales, navbar premium con subrayados animados, botones con degradado y micro-interacciones, tarjetas de estado con riel de color y barra de progreso, filtros tipo chip con contadores en vivo, toasts flotantes, buscador global, tour guiado y estilos del tutorial. Respeta el tema claro/oscuro existente.
- **Nuevo `js/ui-kit.js`:** notificaciones toast (también intercepta `showSuccess`/`showError`), buscador global `Ctrl+K` para saltar a cualquier módulo, botón flotante "¿Cómo usar?" y **tour guiado automático por página** (se muestra una vez; reiniciable desde el botón).
- **Nueva página `pages/tutorial.html`:** tutorial interactivo con flujo del taller paso a paso (clicable), tarjetas por módulo, explicación de roles, checklist de dominio con progreso guardado, mini-quiz con retroalimentación y preguntas frecuentes. Accesible desde el navbar, el login y los paneles.
- **Login (`index.html`):** animaciones de entrada mejoradas, botón para mostrar/ocultar contraseña, chips de acceso rápido con las cuentas demo (admin, mecanico, recepcionista, almacen) y enlace al tutorial.
- **Dashboard:** KPIs con contador animado, flujo del taller interactivo (clic en cada paso muestra qué hace), tarjeta destacada hacia el tutorial.
- **Recepción:** filtros convertidos a chips con contadores por estado, tarjetas con riel de estado y barra de progreso del flujo, skeletons de carga y empty states.
- **Cotización:** resumen de total en caja destacada con animación, chips de items seleccionados con eliminación rápida, listado de cotizaciones con tarjetas de estado, Enter agrega servicios.
- **Inventario:** buscador instantáneo de productos, barra visual de stock por producto (rojo/verde), alertas de stock rediseñadas, modales con cierre por Esc y clic fuera.
- **Órdenes de trabajo:** tarjetas con barra de progreso por estado, chips de filtro con contadores, eliminados `console.log` de depuración y corregido un `<span>` mal cerrado; ya no depende de colores fijos para tema claro.
- **Panel Mecánico:** tarjetas de OT con progreso y botones con confirmación toast.
- **Pago y Entrega:** tarjetas rediseñadas con monto destacado y badges de pago/entrega; corregido bug de template string en la confirmación de entrega.
- **Panel Recepcionista:** ahora muestra estadísticas reales (recepciones activas, cotizaciones pendientes/aprobadas), últimas cotizaciones y accesos rápidos.
- **Navbar:** enlace fijo a **Tutorial** para todos los roles + botón de búsqueda.

</details>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## ⚪ 1.0.0 — 2026-09-14

<img src="./readme-assets/ver-100.svg" width="150" alt="v1.0.0 base" />

> La base que sostiene todo: se acabaron los errores 500 fantasma, los pagos duplicados y el caché terco.

<details>
<summary><b>🐛 Correccion de errores — Backend (clic para ver el detalle)</b></summary>

<br>

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

</details>

<details>
<summary><b>🖥️ Correccion de errores — Frontend (clic para ver el detalle)</b></summary>

<br>

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

</details>

<details>
<summary><b>🗄️ Correccion de errores — Base de datos</b></summary>

<br>

- Se agregó `CONVERTIDA` a la restricción `cotizacion_estado_check`.
- Se agregó `LEFT JOIN FETCH` a `PagoEntregaRepository.findByOrdenTrabajoId()`.
- Se agregó `LEFT JOIN FETCH` a `OtProductoUsadoRepository.findByOrdenTrabajoId()`.

</details>

### ⏳ Problemas conocidos / Mejoras futuras

- Considerar la creación de excepciones específicas como `NotFoundException` y `ConflictException` para utilizar códigos HTTP más precisos, como 404 y 409.
- Actualmente, todas las `RuntimeException` se responden con código HTTP 400.

<br>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

<div align="center">

**AutoGestion · Taller Mecanico Automotriz**
<br>
[⬆ Volver a versiones](#️-versiones)

</div>
