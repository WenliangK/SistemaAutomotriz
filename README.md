<div align="center">

<img src="./readme-assets/banner.svg" alt="AutoGestion - Sistema de Gestion para Taller Mecanico" width="100%" />

<br>

<img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Boot-3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
<img src="https://img.shields.io/badge/Auth-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />

<br><br>

**Sistema web completo de gestion de servicios e inventario para un taller mecanico automotriz.**
<br>
Interfaz de estilo industrial (acero, aceite, senalizacion), modo oscuro y animaciones fluidas.

</div>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 📑 Contenido

| | | |
| :-- | :-- | :-- |
| [📁 Estructura](#-estructura-del-proyecto) | [🧰 Stack](#-stack-tecnologico) | [🚀 Como ejecutar](#-como-ejecutar) |
| [🎭 Casos de uso](#-modelo-de-caso-de-uso-de-negocio-actualizado) | [🔑 Credenciales](#-credenciales-de-prueba) | [🗄️ Datos semilla](#%EF%B8%8F-que-hay-en-la-base-de-datos-automatico) |
| [🔄 Flujo del negocio](#-flujo-del-negocio) | [🧾 Facturacion](#-facturacion-boletafactura--v130) | [🖥️ Pantallas](#%EF%B8%8F-pantallas-del-sistema) |
| [🏗️ Arquitectura](#%EF%B8%8F-arquitectura-del-sistema) | [🔒 Seguridad](#-seguridad) | [🎨 Tema y animaciones](#-tema-y-animaciones) |
| [🔌 API REST](#-api-rest-endpoints) | [🧬 Base de datos](#-modelo-de-base-de-datos-14-tablas) | [🛠️ Problemas comunes](#%EF%B8%8F-resolver-problemas-comunes) |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 📁 Estructura del Proyecto

<details open>
<summary><b>Ver arbol de carpetas</b></summary>

```text
sistema-de-autogestion/
├── README.md                          ← ESTE ARCHIVO
├── CHANGELOG.md
├── docker-compose.yml
├── readme-assets/                     (banner.svg, divider.svg, flow.svg)
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/autogestion/
│       ├── AutogestionApplication.java
│       ├── config/                (Security, JWT, CORS, DataInitializer)
│       ├── entity/                (13 entidades JPA)
│       ├── repository/            (14 repositorios Spring Data)
│       ├── service/               (9 servicios con logica de negocio)
│       ├── controller/            (10 endpoints REST)
│       └── dto/                   (11 request/response DTOs)
├── database/
│   ├── Dockerfile
│   ├── init.sql                   (Schema PostgreSQL + datos semilla)
│   └── migrations/                (parches SQL para volumenes existentes)
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── index.html                 (Login)
│   ├── css/
│   │   ├── styles.css             (Diseno principal ~1160 lineas)
│   │   ├── animations.css         (Animaciones, textures, skeletons)
│   │   └── redesign.css           (Rediseño v1.1.0: gradientes, chips, tour)
│   ├── js/
│   │   ├── api.js                 (Cliente API, navbar, helpers, permisos por rol)
│   │   ├── animations.js          (Transiciones, ripples, stagger)
│   │   ├── ui-kit.js              (Toasts, buscador Ctrl+K, tour guiado)
│   │   ├── confirm-modal.js       (Modal de confirmacion reutilizable)
│   │   └── icons.js               (Iconografia SVG)
│   └── pages/
│       ├── dashboard.html         (Indicadores)
│       ├── recepcion.html         (Recepcion de vehiculos)
│       ├── cotizacion.html        (Diagnostico + Cotizacion)
│       ├── orden_trabajo.html     (Ordenes de trabajo)
│       ├── inventario.html        (Gestion de inventario)
│       ├── pago_entrega.html      (Pagos y entregas + boleta/factura)
│       ├── mecanico.html          (Panel mecanico)
│       ├── recepcionista.html     (Panel recepcionista)
│       ├── tutorial.html          (Tutorial interactivo)
│       └── acceso_denegado.html   (Pagina de error 403)
```

</details>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🧰 Stack Tecnologico

| Capa | Tecnologia |
| :-- | :-- |
| 🖼️ **Frontend** | HTML + CSS + JavaScript + Animaciones CSS custom |
| ⚙️ **Backend** | Java 17+ / Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security) |
| 🗄️ **Base de datos** | PostgreSQL 16 (via Docker) |
| 🔐 **Autenticacion** | JWT (JSON Web Tokens) |
| 👥 **Roles** | `ADMIN` · `MECANICO` · `RECEPCIONISTA` · `ALMACENERO` |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🚀 Como Ejecutar

### Lo que necesitas

- **Docker** → https://docs.docker.com/get-docker/
- **Docker Compose** (viene incluido con Docker Desktop)

### Paso 1 — Levantar el sistema

```bash
docker compose up --build
```

Esto construye e inicia 3 contenedores:

| Contenedor | Servicio | Puerto |
| :-- | :-- | :-- |
| `db` | PostgreSQL 16 | 5433 (host) → 5432 (contenedor) |
| `backend` | Spring Boot | 8080 |
| `frontend` | Nginx | 5500 |

> [!NOTE]
> Espera hasta que veas los logs de los contenedores iniciando.

### Paso 2 — Abrir el navegador

- **Frontend:** http://localhost:5500
- **Backend API:** http://localhost:8080/api

### Paso 3 — Iniciar sesion

- **Email:** `admin@sanmartin.pe`
- **Contrasena:** `admin123`

### Paso 4 — Recorrer el flujo del negocio

```text
Login -> Dashboard -> Recepcion -> Cotizacion -> Ordenes de Trabajo -> Inventario -> Pago/Entrega
```

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🎭 Modelo de Caso de Uso de Negocio (Actualizado)

Actores: **Cliente, Recepcionista, Mecanico, Administrador**.

> El Recepcionista es el actor clave que registra la llegada del cliente/vehiculo, inicia la recepcion y coordina el flujo inicial del taller.

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔑 Credenciales de Prueba

| Usuario | Email | Contrasena | Rol |
| :-- | :-- | :-- | :-- |
| Admin Taller | `admin@sanmartin.pe` | `admin123` | `ADMIN` |
| Mecanico Uno | `mecanico1@sanmartin.pe` | `admin123` | `MECANICO` |
| Recepcionista | `recepcionista@sanmartin.pe` | `admin123` | `RECEPCIONISTA` |
| Almacenero | `almacen@sanmartin.pe` | `admin123` | `ALMACENERO` |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🗄️ Que Hay en la Base de Datos (Automatico)

Al levantar Docker, se insertan automaticamente estos datos:

| Dato | Cantidad |
| :-- | :-- |
| 👥 Usuarios | 3 (admin, mecanico, almacenero) |
| 🧑‍💼 Clientes | 3 (Juan Perez, Maria Lopez, Carlos Garcia) |
| 🚗 Vehiculos | 3 (Toyota Corolla, Hyundai Accent, Nissan Sentra) |
| 🛠️ Servicios | 8 (cambio de aceite, alineacion, frenos, etc.) |
| 📦 Productos | 12 (aceite, filtros, pastillas, discos, etc.) |

> [!IMPORTANT]
> Los datos se mantienen en PostgreSQL dentro del contenedor Docker. Si eliminas el volumen `pgdata`, se perderan y se volveran a insertar al reiniciar.

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔄 Flujo del Negocio

<div align="center">
  <img src="./readme-assets/flow.svg" alt="Flujo del negocio" width="100%" />
</div>

<br>

```text
1. RECEPCION
   └-> Registrar cliente + vehiculo + problema reportado

2. DIAGNOSTICO
   └-> El mecanico describe los hallazgos tecnicos

3. COTIZACION
   └-> Seleccionar servicios y productos
   └-> El total se calcula automaticamente
   └-> Aprobar o rechazar

4. ORDEN DE TRABAJO
   └-> Se crea automaticamente al aprobar cotizacion
   └-> Se asigna a un mecanico
   └-> Se cambia de estado: PENDIENTE -> EN_PROCESO -> EN_PRUEBA -> FINALIZADA

5. EJECUCION
   └-> El mecanico registra productos usados
   └-> El stock se descuenta automaticamente (transaccion atomica)

6. PAGO Y ENTREGA
   └-> Se registra el pago (metodo + comprobante: BOLETA / FACTURA)
   └-> Boleta: DNI/CE/PAS + nombre. Factura: RUC 11 + razon social + direccion fiscal
   └-> Desglose: Subtotal + IGV (18%) + Total, con serie/numero correlativo
   └-> Se registra la entrega del vehiculo (bloqueada hasta tener pago con comprobante)
```

> [!NOTE]
> El comprobante actual es **interno / pre-electronico**: no hay integracion con SUNAT (sin PSE/OSE, sin QR SUNAT, sin envio a SUNAT). Ver [🧾 Facturacion](#-facturacion-boletafactura--propuesta-v130) para el diseno propuesto en v1.3.0.

Estados de la orden de trabajo:

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE
    PENDIENTE --> EN_PROCESO
    EN_PROCESO --> EN_PRUEBA
    EN_PRUEBA --> FINALIZADA
    FINALIZADA --> [*]
```

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🖥️ Pantallas del Sistema

| Pantalla | Descripcion |
| :-- | :-- |
| 🔐 **Login** | Split-screen: identidad de marca (izq) + formulario (der), toggle de temas |
| 📊 **Dashboard** | 4 indicadores animados + flujo visual + alertas de stock + accesos rapidos |
| 🧾 **Recepcion** | Formulario cliente/vehiculo (izq) + lista recepciones (der) |
| 💬 **Cotizacion** | Diagnostico + cotizacion con calculo en vivo + cotizaciones existentes |
| 🔧 **Ordenes de Trabajo** | Lista de OT con badges de estado + modal productos usados |
| 📦 **Inventario** | Tabla de productos + entrada/ajuste + alertas de stock |
| 💳 **Pago/Entrega** | Ordenes finalizadas + resumen animado + wizard BOLETA/FACTURA en 3 pasos con vista previa + constancia estilo ticket que imprime igual que en pantalla y se descarga en HTML (v1.4.1) |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🧾 Facturacion (Boleta/Factura) — v1.3.0

> [!NOTE]
> **Frontend implementado** en `frontend/pages/pago_entrega.html`: modal de pago con selector BOLETA/FACTURA, validacion DNI/RUC, metodo de pago, desglose IGV y constancia imprimible (boton Imprimir + Reimprimir). El comprobante se guarda en `localStorage` (`ag_comprobantes_v1`, series `B001`/`F001` con correlativo). **Backend pendiente**: `pago_entrega` en BD aun solo guarda `monto` + fechas, asi que el comprobante todavia no persiste en servidor (ver gap 1-3).

### Gaps que quedan en backend

| # | Gap | Estado |
| :-- | :-- | :-- |
| 1 | `pago_entrega` sin `metodo_pago`, `tipo_comprobante`, `serie`, `numero`, `subtotal`, `igv` | ⏳ Pendiente (migracion `2026-10-01_comprobantes.sql`) |
| 2 | `cliente` sin `tipo_documento`, `razon_social`, `direccion_fiscal` | ⏳ Pendiente |
| 3 | `PagoRequest` solo lleva `monto` | ⏳ Pendiente (payload extendido) |
| 4 | `pago_entrega.html` sin selector ni constancia | ✅ Hecho en v1.3.0 frontend |
| 5 | `total` sin desglose Subtotal/IGV en servidor | ✅ Desglose en frontend (`total/1.18`); pendiente en BD |

### Boleta vs Factura en Peru

| | 🧾 **Boleta** | 🧾 **Factura** |
| :-- | :-- | :-- |
| **Para quien** | Consumidor final (persona natural) | Empresa / cliente que necesita credito fiscal |
| **Documento** | DNI (8) / CE / PAS | RUC (11) obligatorio |
| **Datos extra** | Solo nombre | Razon social + direccion fiscal obligatorias |
| **IGV** | Incluido en el total (se muestra referencial) | Desglosado: Subtotal + IGV 18% + Total |
| **Serie sugerida** | `B001` | `F001` |

### Flujo en `pago_entrega.html` (implementado)

```text
OT FINALIZADA
  └-> [Registrar Pago] abre modal:
       1. Tipo comprobante: (•) Boleta  ( ) Factura
       2. Documento: DNI 8 (boleta) o RUC 11 + razon social + direccion (factura)
       3. Metodo pago: EFECTIVO · YAPE · PLIN · TRANSFERENCIA · TARJETA
       4. Desglose auto: Subtotal + IGV 18% + Total (= cotizacion.total)
       5. [Confirmar pago] -> POST al backend + guarda comprobante local
  └-> Constancia automatica: ticket con serie/numero + [Imprimir]
  └-> Tarjeta muestra badge BOLETA B001-000123 o FACTURA F001-000045
  └-> [Registrar Entrega] sigue bloqueado hasta tener pago registrado
  └-> [Ver comprobante] reimprime la constancia en cualquier momento
```

```mermaid
stateDiagram-v2
    [*] --> FINALIZADA
    FINALIZADA --> PAGADA : Registrar pago\n+ comprobante
    PAGADA --> ENTREGADA : Registrar entrega
    ENTREGADA --> [*]
    PAGADA --> ANULADA : Anular comprobante
```

### Cambios de datos y API (resumen)

```text
cliente  + tipo_documento (DNI/RUC/CE/PAS) + razon_social + direccion_fiscal
pago_entrega  + metodo_pago + tipo_comprobante (BOLETA/FACTURA) + serie + numero
              + subtotal + igv + total + dni_ruc_snapshot + razon_social_snapshot
              + estado_comprobante (EMITIDO/ANULADO)
```

| Metodo | Endpoint (propuesto) | Descripcion |
| :-: | :-- | :-- |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/pagos` (payload extendido) | Registrar pago con `metodo_pago` + `tipo_comprobante` + datos fiscales |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/comprobantes/{id}` | Obtener comprobante para reimprimir |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/comprobantes/serie` | Siguiente correlativo por serie (B001/F001) |

> [!WARNING]
> Alcance: comprobante **interno imprimible** (ticket/A4 con logo, serie/numero, detalle de servicios/productos, IGV y metodo de pago). **No** incluye facturacion electronica SUNAT (sin PSE/OSE, QR ni envio a SUNAT); eso seria una fase v2 con proveedor electronico.

Ver detalle de migracion y archivos a tocar en `CHANGELOG.md` → `[1.3.0]`.

### ✨ Funcionalidades visuales

- **Guia contextual** en cada pagina: banner "que hago aqui" en 3 pasos + boton al siguiente paso (v1.4.0, se puede ocultar)
- **Responsive real**: tablet a 2 columnas, navbar movil con scroll, modales adaptados (v1.4.0)
- **Modo oscuro/claro/sistema** con toggle flotante en todas las paginas
- **Animaciones de entrada escalonadas** (stagger) en indicadores, tablas y cards
- **Transiciones de pagina** tipo SPA-light al navegar entre secciones
- **Microinteracciones**: ripple en botones, pop en badges, elevacion en hover
- **Skeletons de carga** y estados vacios estilizados
- **Textura sutil tipo acero** en todas las superficies
- **Flujo visual** con dots animados (pulso) en pasos del proceso

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🏗️ Arquitectura del Sistema

```mermaid
flowchart LR
    F["🖼️ Frontend<br/>HTML / JS<br/>Bootstrap · JWT"] -->|API| C["🎛️ Controller<br/>REST<br/>@RestController · JWT Filter"]
    C --> S["⚙️ Service<br/>Logica<br/>@Service · @Transactional"]
    S --> D[("🗄️ PostgreSQL<br/>DB<br/>Docker · Puerto 5432")]
```

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔒 Seguridad

- **Autenticacion:** JWT (JSON Web Token)
- **Roles:** ADMIN, MECANICO, ALMACENERO
- **Endpoints protegidos** por rol con `@PreAuthorize`
- **Passwords** hasheados con BCrypt

### Permisos por Rol

| Rol | Puede hacer |
| :-- | :-- |
| 🛡️ **ADMIN** | Dashboard, recepcion, cotizacion, ordenes, inventario, pago/entrega, reportes (los paneles de mecanico y recepcionista son exclusivos de su rol) |
| 🔧 **MECANICO** | Panel Mecanico, sus ordenes de trabajo (cambio de estado), inventario en lectura |
| 🧾 **RECEPCIONISTA** | Panel Recepcionista, recepcion, cotizacion, ordenes de trabajo |
| 📦 **ALMACENERO** | Dashboard, inventario (productos, movimientos, alertas de stock) |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🎨 Tema y Animaciones

### Cambiar tema

Haz click en el boton flotante (esquina inferior derecha) para ciclar:

- **Sistema** (sigue el tema del SO)
- **Claro**
- **Oscuro**

La preferencia se guarda en `localStorage` y persiste entre sesiones.

### Animaciones

Todas las animaciones se desactivan automaticamente si el SO tiene activado `prefers-reduced-motion`. Puedes probarlo en:

- **Windows:** Configuracion > Accesibilidad > Efectos visuales > Animaciones
- **Mac:** Sistema > Accesibilidad > Pantalla > Reducir movimiento

### Helpers JS (consola del navegador)

```js
agSkeleton(container, 'table')    // Muestra skeleton de carga
agEmptyState(container, null, 'Sin datos', 'Descripcion')  // Estado vacio
agFlashRow(tableRow)              // Resalta una fila momentaneamente
```

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🌐 URLs del Sistema

| Servicio | URL |
| :-- | :-- |
| Frontend | http://localhost:5500 |
| Backend API | http://localhost:8080/api |

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🔌 API REST Endpoints

<details open>
<summary><b>Ver los 23 endpoints</b></summary>

<br>

| Metodo | Endpoint | Descripcion |
| :-: | :-- | :-- |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/auth/login` | Autenticacion (devuelve JWT) |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/clientes` | Crear cliente |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/clientes` | Listar clientes |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/vehiculos` | Crear vehiculo |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/vehiculos` | Listar vehiculos |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/recepciones` | Crear recepcion |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/recepciones` | Listar recepciones (filtro por estado) |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/diagnosticos` | Crear diagnostico |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/cotizaciones` | Crear cotizacion |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/cotizaciones` | Listar cotizaciones |
| ![PUT](https://img.shields.io/badge/PUT-FCA130?style=flat-square) | `/api/cotizaciones/{id}/aprobar` | Aprobar cotizacion |
| ![PUT](https://img.shields.io/badge/PUT-FCA130?style=flat-square) | `/api/cotizaciones/{id}/rechazar` | Rechazar cotizacion |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/ordenes-trabajo` | Crear orden de trabajo |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/ordenes-trabajo` | Listar OT |
| ![PUT](https://img.shields.io/badge/PUT-FCA130?style=flat-square) | `/api/ordenes-trabajo/{id}/estado` | Cambiar estado de OT |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/ordenes-trabajo/{id}/productos-usados` | Registrar producto usado |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/productos` | Listar productos |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/productos` | Crear producto |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/inventario/movimientos` | Registrar movimiento de inventario |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/inventario/alertas` | Obtener alertas de stock bajo |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/pagos` | Registrar pago |
| ![POST](https://img.shields.io/badge/POST-49CC90?style=flat-square) | `/api/entregas/{id}` | Registrar entrega |
| ![GET](https://img.shields.io/badge/GET-61AFFE?style=flat-square) | `/api/reportes/indicadores` | Obtener indicadores del dashboard |

</details>

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🧬 Modelo de Base de Datos (14 tablas)

```text
usuario ──────────┐
                   │
cliente ──┐        │   (+ tipo_documento, razon_social, direccion_fiscal en v1.3.0)
          │        │
vehiculo ─┘        │
      │            │
recepcion ─────────┤
      │            │
diagnostico ───────┤
      │            │
servicio ──────────┤
      │            │
producto ──────────┤
      │            │
cotizacion ────────┤
   ├── cotizacion_servicio
   └── cotizacion_producto
      │
orden_trabajo ─────┤
   └── ot_producto_usado
      │
inventario_movimiento
      │
pago_entrega  (+ metodo_pago, tipo_comprobante BOLETA/FACTURA, serie/numero, subtotal/igv/total en v1.3.0)
```

> [!TIP]
> v1.3.0: el pago genera comprobante interno (serie `B001`/`F001` + correlativo + snapshot fiscal) con constancia imprimible. Detalle en [🧾 Facturacion](#-facturacion-boletafactura--v130) y en `CHANGELOG.md`.

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

## 🛠️ Resolver Problemas Comunes

<details>
<summary><b>"Puerto 8080 ya en uso"</b></summary>

<br>

Cierra otras aplicaciones que puedan estar usando ese puerto, o cambia el puerto en `docker-compose.yml`:

```yaml
backend:
  ports:
    - "8081:8080"
```

</details>

<details>
<summary><b>"Docker no encontrado"</b></summary>

<br>

Instala Docker Desktop desde https://docs.docker.com/get-docker/

</details>

<details>
<summary><b>"Los contenedores no inician"</b></summary>

<br>

Verifica que Docker este corriendo. Puedes revisar los logs con:

```bash
docker compose logs
```

</details>

<details>
<summary><b>"Los datos se perdieron"</b></summary>

<br>

Si eliminaste el volumen `pgdata`, se pierden los datos. Al reiniciar Docker se vuelven a insertar automaticamente.

</details>

<details>
<summary><b>"Recibo 403 o error CORS"</b></summary>

<br>

Asegurate de que:

1. El frontend se sirve por HTTP (no `file://`)
2. El backend esta corriendo en el puerto 8080
3. Usas `http://localhost:5500` (no doble clic al HTML)

</details>

<br>

<div align="center">

<img src="./readme-assets/divider.svg" width="100%" height="6" alt="" />

**Desarrollado para el taller mecanico automotriz**

</div>
