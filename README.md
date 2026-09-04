# AutoGestion - Sistema de Gestion para Taller Mecanico

Sistema web completo de gestion de servicios e inventario para un taller mecanico automotriz.
Interfaz de estilo industrial (acero, aceite, senalizacion), modo oscuro, y animaciones fluidas.

---

## Estructura del Proyecto

```
sistema-de-autogestion/
├── README.md                          ← ESTE ARCHIVO
└── sistema-de-autogestion/            ← Codigo fuente del proyecto
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
    │   └── init.sql                   (Schema PostgreSQL + datos semilla)
    ├── frontend/
    │   ├── Dockerfile
    │   ├── nginx.conf
    │   ├── index.html                 (Login)
    │   ├── css/
    │   │   ├── styles.css             (Diseno principal ~1160 lineas)
    │   │   └── animations.css         (Animaciones, textures, skeletons)
    │   ├── js/
    │   │   ├── api.js                 (Cliente API, navbar, helpers)
    │   │   └── animations.js          (Transiciones, ripples, stagger)
    │   └── pages/
    │       ├── dashboard.html         (Indicadores)
    │       ├── recepcion.html         (Recepcion de vehiculos)
    │       ├── cotizacion.html        (Diagnostico + Cotizacion)
    │       ├── orden_trabajo.html     (Ordenes de trabajo)
    │       ├── inventario.html        (Gestion de inventario)
    │       └── pago_entrega.html      (Pagos y entregas)
    └── docker-compose.yml
```

---

## Stack Tecnologico

| Capa | Tecnologia |
|------|-----------|
| **Frontend** | HTML + CSS + JavaScript + Animaciones CSS custom |
| **Backend** | Java 17+ / Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security) |
| **Base de datos** | H2 en memoria (para presentaciones) / PostgreSQL (para produccion con Docker) |
| **Autenticacion** | JWT (JSON Web Tokens) |
| **Roles** | ADMIN, MECANICO, ALMACENERO |

---

## Como Ejecutar (Presentacion en Clase)

### Lo que necesitas
- **Java 17 o superior** (https://adoptium.net/)
- **Maven** (https://maven.apache.org/download.cgi) — solo para compilar el backend
- **Node.js** (opcional) — para usar `npx serve` y servir el frontend

### Paso 1 — Compilar el backend (crear el JAR)
Abre una terminal en la carpeta del backend:
```bash
cd sistema-de-autogestion/sistema-de-autogestion/backend
mvn clean package -DskipTests
```
Esto descarga las dependencias (primera vez, ~2 min) y crea el archivo:
```
backend/target/autogestion-backend-1.0.0.jar  (~52MB)
```
> El JAR NO se sube a GitHub (pesa 52MB). Se genera desde el codigo fuente con el comando anterior.

### Paso 2 — Arrancar el backend
```bash
java -jar target/autogestion-backend-1.0.0.jar
```
Espera hasta que veas:
```
Started AutogestionApplication in X seconds
```
> No cierres esta terminal mientras presentas.

### Paso 3 — Servir el frontend por HTTP (IMPORTANTE)

**NO abras los archivos HTML con doble clic** (usando `file://`). El navegador bloquea las llamadas API por CORS.

**Opcion A — npx serve (recomendado, sin instalar nada):**
```bash
cd sistema-de-autogestion/sistema-de-autogestion/frontend
npx serve . -l 5500
```
Luego abre `http://localhost:5500` en el navegador.
> La primera vez te pedira instalar `serve`, dale que si.

**Opcion B — VS Code Live Server:**
1. Abre la carpeta `frontend/` en VS Code
2. Instala la extension "Live Server" si no la tienes
3. Click derecho en `index.html` -> "Open with Live Server"
4. Se abre en `http://localhost:5500`

**Opcion C — Usar el nginx del Docker:**
```bash
cd sistema-de-autogestion
docker-compose up frontend
```
Se abre en `http://localhost:5500`

### Paso 4 — Iniciar sesion
- **Email:** `admin@sanmartin.pe`
- **Contrasena:** `admin123`

### Paso 5 — Recorrer el flujo del negocio
```
Login -> Dashboard -> Recepcion -> Cotizacion -> Ordenes de Trabajo -> Inventario -> Pago/Entrega
```

---

## Credenciales de Prueba

| Usuario | Email | Contrasena | Rol |
|---------|-------|------------|-----|
| Admin Taller | admin@sanmartin.pe | admin123 | ADMIN |
| Mecanico Uno | mecanico1@sanmartin.pe | admin123 | MECANICO |
| Almacenero | almacen@sanmartin.pe | admin123 | ALMACENERO |

---

## Que Hay en la Base de Datos (Automatico)

Al arrancar el JAR, se insertan automaticamente estos datos:

| Dato | Cantidad |
|------|----------|
| Usuarios | 3 (admin, mecanico, almacenero) |
| Clientes | 3 (Juan Perez, Maria Lopez, Carlos Garcia) |
| Vehiculos | 3 (Toyota Corolla, Hyundai Accent, Nissan Sentra) |
| Servicios | 8 (cambio de aceite, alineacion, frenos, etc.) |
| Productos | 12 (aceite, filtros, pastillas, discos, etc.) |

> La base de datos es H2 en memoria — se pierde al cerrar el servidor. Pero cada vez que lo vuelvas a arrancar, los datos semilla se insertan automaticamente.

---

## Flujo del Negocio

```
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
   └-> Se registra el pago
   └-> Se registra la entrega del vehiculo
```

---

## Pantallas del Sistema

| Pantalla | Descripcion |
|----------|------------|
| **Login** | Split-screen: identidad de marca (izq) + formulario (der), toggle de temas |
| **Dashboard** | 4 indicadores animados + flujo visual + alertas de stock + accesos rapidos |
| **Recepcion** | Formulario cliente/vehiculo (izq) + lista recepciones (der) |
| **Cotizacion** | Diagnostico + cotizacion con calculo en vivo + cotizaciones existentes |
| **Ordenes de Trabajo** | Lista de OT con badges de estado + modal productos usados |
| **Inventario** | Tabla de productos + entrada/ajuste + alertas de stock |
| **Pago/Entrega** | Ordenes finalizadas + resumen del dia + flujo visual |

### Funcionalidades visuales
- **Modo oscuro/claro/sistema** con toggle flotante en todas las paginas
- **Animaciones de entrada escalonadas** (stagger) en indicadores, tablas y cards
- **Transiciones de pagina** tipo SPA-light al navegar entre secciones
- **Microinteracciones**: ripple en botones, pop en badges, elevacion en hover
- **Skeletons de carga** y estados vacios estilizados
- **Textura sutil tipo acero** en todas las superficies
- **Flujo visual** con dots animados (pulso) en pasos del proceso

---

## Arquitectura del Sistema

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌─────────┐
│   Frontend  │────>│  Controller  │────>│   Service    │────>│   H2    │
│  (HTML/JS)  │ API │  (REST)      │     │ (Logica)     │     │  (DB)   │
└─────────────┘     └──────────────┘     └──────────────┘     └─────────┘
     HTML               Spring Boot          Spring Boot        En memoria
   Bootstrap            @RestController      @Service          Base de datos
     JWT                 JWT Filter           @Transactional
```

---

## Seguridad

- **Autenticacion:** JWT (JSON Web Token)
- **Roles:** ADMIN, MECANICO, ALMACENERO
- **Endpoints protegidos** por rol con `@PreAuthorize`
- **Passwords** hasheados con BCrypt

### Permisos por Rol

| Rol | Puede hacer |
|-----|------------|
| **ADMIN** | Todo: gestionar clientes, vehiculos, recepciones, cotizaciones, ordenes, inventario, pagos, entregas, reportes |
| **MECANICO** | Recepciones, diagnosticos, cotizaciones, sus ordenes de trabajo, registrar productos usados |
| **ALMACENERO** | Inventario: ver productos, registrar movimientos, ver alertas de stock |

---

## Tema y Animaciones

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

---

## Version con Docker (Opcional)

Si la computadora tiene Docker instalado, tambien puedes levantar con PostgreSQL:

```bash
cd sistema-de-autogestion
docker compose up --build
```

Esto levanta 3 contenedores:
- **db:** PostgreSQL 16 (puerto 5432)
- **backend:** Spring Boot (puerto 8080)  - **frontend:** Nginx (puerto 5500)

URLs:
- Frontend: http://localhost:5500
- Backend API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console

---

## API REST Endpoints

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | /api/auth/login | Autenticacion (devuelve JWT) |
| POST | /api/clientes | Crear cliente |
| GET | /api/clientes | Listar clientes |
| POST | /api/vehiculos | Crear vehiculo |
| GET | /api/vehiculos | Listar vehiculos |
| POST | /api/recepciones | Crear recepcion |
| GET | /api/recepciones | Listar recepciones (filtro por estado) |
| POST | /api/diagnosticos | Crear diagnostico |
| POST | /api/cotizaciones | Crear cotizacion |
| GET | /api/cotizaciones | Listar cotizaciones |
| PUT | /api/cotizaciones/{id}/aprobar | Aprobar cotizacion |
| PUT | /api/cotizaciones/{id}/rechazar | Rechazar cotizacion |
| POST | /api/ordenes-trabajo | Crear orden de trabajo |
| GET | /api/ordenes-trabajo | Listar OT |
| PUT | /api/ordenes-trabajo/{id}/estado | Cambiar estado de OT |
| POST | /api/ordenes-trabajo/{id}/productos-usados | Registrar producto usado |
| GET | /api/productos | Listar productos |
| POST | /api/productos | Crear producto |
| POST | /api/inventario/movimientos | Registrar movimiento de inventario |
| GET | /api/inventario/alertas | Obtener alertas de stock bajo |
| POST | /api/pagos | Registrar pago |
| POST | /api/entregas/{id} | Registrar entrega |
| GET | /api/reportes/indicadores | Obtener indicadores del dashboard |

---

## Modelo de Base de Datos (14 tablas)

```
usuario ──────────┐
                   │
cliente ──┐        │
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
pago_entrega
```

---

## Resolver Problemas Comunes

### "Puerto 8080 ya en uso"
Cierra otras aplicaciones que puedan estar usando ese puerto, o cambia el puerto en:
```
backend/src/main/resources/application.properties
server.port=8081
```

### "Java no encontrado"
Instala Java desde https://adoptium.net/ (descarga JDK 17 o superior)

### "No se conecta al backend"
Verifica que la terminal donde corre el JAR siga abierta y muestre `Started AutogestionApplication`

### "Los datos se perdieron"
Es normal. La base de datos H2 es en memoria. Al cerrar el servidor se borran. Al volver a arrancar, se insertan los datos semilla automaticamente.

### "Recibo 403 o error CORS"
Asegurate de que:
1. El frontend se sirve por HTTP (no `file://`)
2. El backend esta corriendo en el puerto 8080
3. Usas `http://localhost:5500` (no doble clic al HTML)

### "npx serve no funciona"
Asegurate de estar en la carpeta `frontend/`:
```bash
cd sistema-de-autogestion/sistema-de-autogestion/frontend
npx serve . -l 5500
```
Si no tienes Node.js, descargalo desde https://nodejs.org/

---

**Desarrollado para el taller mecanico automotriz**
