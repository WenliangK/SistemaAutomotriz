# 🔧 AutoGestión - Sistema de Gestión para Taller Mecánico

Sistema web completo de gestión de servicios e inventario para un taller mecánico automotriz.

---

## 📁 Estructura del Proyecto

```
sistema-de-autogestion/
├── README.md                          ← ESTE ARCHIVO
└── sistema-de-autogestion/            ← Código fuente del proyecto
    ├── backend/
    │   ├── Dockerfile
    │   ├── pom.xml
    │   └── src/main/java/com/autogestion/
    │       ├── AutogestionApplication.java
    │       ├── config/                (Security, JWT, CORS, DataInitializer)
    │       ├── entity/                (13 entidades JPA)
    │       ├── repository/            (14 repositorios Spring Data)
    │       ├── service/               (9 servicios con lógica de negocio)
    │       ├── controller/            (10 endpoints REST)
    │       └── dto/                   (11 request/response DTOs)
    ├── database/
    │   ├── Dockerfile
    │   └── init.sql                   (Schema PostgreSQL + datos semilla)
    ├── frontend/
    │   ├── Dockerfile
    │   ├── nginx.conf
    │   ├── index.html                 (Login)
    │   ├── js/api.js                  (Cliente API compartido)
    │   └── pages/
    │       ├── dashboard.html         (Indicadores)
    │       ├── recepcion.html         (Recepción de vehículos)
    │       ├── cotizacion.html        (Diagnóstico + Cotización)
    │       ├── orden_trabajo.html     (Órdenes de trabajo)
    │       ├── inventario.html        (Gestión de inventario)
    │       └── pago_entrega.html      (Pagos y entregas)
    └── docker-compose.yml
```

---

## 🏗️ Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| **Frontend** | HTML + CSS + JavaScript + Bootstrap 5 + Tema claro/oscuro/sistema |
| **Backend** | Java 17+ / Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security) |
| **Base de datos** | H2 en memoria (para presentaciones) / PostgreSQL (para producción con Docker) |
| **Autenticación** | JWT (JSON Web Tokens) |
| **Roles** | ADMIN, MECANICO, ALMACENERO |

---

## 🚀 Cómo Ejecutar (Presentación en Clase)

### Lo que necesitas
- **Java 17 o superior** instalado en la computadora
- **Eso es todo** (no necesita Docker, no necesita PostgreSQL)

### Paso 1 — Copiar archivos a un USB
Copia esta estructura completa a tu USB:
```
USB/
├── README.md
└── sistema-de-autogestion/
    ├── backend/
    │   ├── target/autogestion-backend-1.0.0.jar  ← ARCHIVO PRINCIPAL
    │   └── ...
    ├── frontend/
    │   ├── index.html
    │   ├── js/api.js
    │   └── pages/
    └── ...
```

### Paso 2 — Arrancar el backend
Abre una terminal (CMD o PowerShell) y ejecuta:
```bash
cd USB:\sistema-de-autogestion\backend
java -jar target\autogestion-backend-1.0.0.jar
```
Espera hasta que veas:
```
Started AutogestionApplication in X seconds
```
> ⚠️ **No cierres esta terminal** mientras presentas. El servidor debe seguir corriendo.

### Paso 3 — Servir el frontend por HTTP (IMPORTANTE)

**NO abras los archivos HTML con doble clic** (usando `file://`). El navegador bloquea las llamadas API por CORS.

**Opcion A — VS Code Live Server (recomendado):**
1. Abre la carpeta `frontend/` en VS Code
2. Instala la extension "Live Server" si no la tienes
3. Click derecho en `index.html` -> "Open with Live Server"
4. Se abre en `http://localhost:5500`

**Opcion B — Python (si no tienes VS Code):**
```bash
cd USB:\sistema-de-autogestion\frontend
python -m http.server 5500
```
Luego abre `http://localhost:5500` en el navegador.

**Opcion C — Usar el nginx del Docker:**
```bash
cd USB:\sistema-de-autogestion\docker-compose up frontend
```
Se abre en `http://localhost:3000`

### Paso 4 — Iniciar sesión
- **Email:** `admin@sanmartin.pe`
- **Contraseña:** `admin123`

### Paso 5 — Recorrer el flujo del negocio
```
Login → Dashboard → Recepción → Cotización → Órdenes de Trabajo → Inventario → Pago/Entrega
```

---

## 🔑 Credenciales de Prueba

| Usuario | Email | Contraseña | Rol |
|---------|-------|------------|-----|
| Admin Taller | admin@sanmartin.pe | admin123 | ADMIN |
| Mecánico Uno | mecanico1@sanmartin.pe | admin123 | MECANICO |
| Almacenero | almacen@sanmartin.pe | admin123 | ALMACENERO |

---

## 📊 Qué Hay en la Base de Datos (Automático)

Al arrancar el JAR, se insertan automáticamente estos datos:

| Dato | Cantidad |
|------|----------|
| Usuarios | 3 (admin, mecánico, almacenero) |
| Clientes | 3 (Juan Pérez, María López, Carlos García) |
| Vehículos | 3 (Toyota Corolla, Hyundai Accent, Nissan Sentra) |
| Servicios | 8 (cambio de aceite, alineación, frenos, etc.) |
| Productos | 12 (aceite, filtros, pastillas, discos, etc.) |

> 💡 **La base de datos es H2 en memoria** → Se pierde al cerrar el servidor. Pero cada vez que lo vuelvas a arrancar, los datos semilla se insertan automáticamente.

---

## 🔄 Flujo del Negocio

```
1. RECEPCIÓN
   └→ Registrar cliente + vehículo + problema reportado

2. DIAGNÓSTICO
   └→ El mecánico describe los hallazgos técnicos

3. COTIZACIÓN
   └→ Seleccionar servicios y productos
   └→ El total se calcula automáticamente
   └→ Aprobar o rechazar

4. ORDEN DE TRABAJO
   └→ Se crea automáticamente al aprobar cotización
   └→ Se asigna a un mecánico
   └→ Se cambia de estado: PENDIENTE → EN_PROCESO → EN_PRUEBA → FINALIZADA

5. EJECUCIÓN
   └→ El mecánico registra productos usados
   └→ El stock se descuenta automáticamente (transacción atómica)

6. PAGO Y ENTREGA
   └→ Se registra el pago
   └→ Se registra la entrega del vehículo
```

---

## 📋 Pantallas del Sistema

| Pantalla | Descripción |
|----------|------------|
| **Login** | Formulario centrado con tarjeta, validacion de campos |
| **Tema** | Boton flotante: claro / oscuro / sistema (respeta el SO) |
| **Dashboard** | 4 tarjetas de indicadores + alertas de stock |
| **Recepción** | Formulario cliente + vehículo + problema, tabla de recepciones |
| **Cotización** | Diagnóstico + selector servicios/productos + total en vivo |
| **Órdenes de Trabajo** | Tabla con badges de estado, modal productos usados |
| **Inventario** | Tabla con stock resaltado en rojo, movimientos de entrada/ajuste |
| **Pago/Entrega** | Resumen del día, botones de acción |

---

## ⚙️ Arquitectura del Sistema

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌─────────┐
│   Frontend  │────▶│  Controller  │────▶│   Service    │────▶│   H2    │
│  (HTML/JS)  │ API │  (REST)      │     │ (Lógica)     │     │  (DB)   │
└─────────────┘     └──────────────┘     └──────────────┘     └─────────┘
     HTML               Spring Boot          Spring Boot        En memoria
   Bootstrap            @RestController      @Service          Base de datos
     JWT                 JWT Filter           @Transactional
```

---

## 🔐 Seguridad

- **Autenticación:** JWT (JSON Web Token)
- **Roles:** ADMIN, MECANICO, ALMACENERO
- **Endpoints protegidos** por rol con `@PreAuthorize`
- **Passwords** hasheados con BCrypt

### Permisos por Rol

| Rol | Puede hacer |
|-----|------------|
| **ADMIN** | Todo: gestionar clientes, vehículos, recepciones, cotizaciones, órdenes, inventario, pagos, entregas, reportes |
| **MECANICO** | Recepciones, diagnósticos, cotizaciones, sus órdenes de trabajo, registrar productos usados |
| **ALMACENERO** | Inventario: ver productos, registrar movimientos, ver alertas de stock |

---

## 🐳 Versión con Docker (Opcional)

Si la computadora tiene Docker instalado, también puedes levantar con PostgreSQL:

```bash
cd sistema-de-autogestion
docker compose up --build
```

Esto levanta 3 contenedores:
- **db:** PostgreSQL 16 (puerto 5432)
- **backend:** Spring Boot (puerto 8080)
- **frontend:** Nginx (puerto 3000)

URLs:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console

---

## 📊 API REST Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/login | Autenticación (devuelve JWT) |
| POST | /api/clientes | Crear cliente |
| GET | /api/clientes | Listar clientes |
| POST | /api/vehiculos | Crear vehículo |
| GET | /api/vehiculos | Listar vehículos |
| POST | /api/recepciones | Crear recepción |
| GET | /api/recepciones | Listar recepciones (filtro por estado) |
| POST | /api/diagnosticos | Crear diagnóstico |
| POST | /api/cotizaciones | Crear cotización |
| GET | /api/cotizaciones | Listar cotizaciones |
| PUT | /api/cotizaciones/{id}/aprobar | Aprobar cotización |
| PUT | /api/cotizaciones/{id}/rechazar | Rechazar cotización |
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

## 🗄️ Modelo de Base de Datos (14 tablas)

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

## 🛠️ Resolver Problemas Comunes

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
3. Usas `http://localhost:5500` o `http://localhost:3000` (no doble clic al HTML)

### Tema Claro / Oscuro / Sistema
- Haz click en el boton flotante (esquina inferior derecha) para ciclar entre temas
- **Sistema**: respeta el tema del sistema operativo automaticamente
- **Claro**: fuerza tema claro
- **Oscuro**: fuerza tema oscuro
- La preferencia se guarda y persiste entre sesiones

---

**Desarrollado para el taller mecanico automotriz**
