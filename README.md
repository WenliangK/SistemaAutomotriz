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
| **Base de datos** | PostgreSQL 16 (via Docker) |
| **Autenticacion** | JWT (JSON Web Tokens) |
| **Roles** | ADMIN, MECANICO, ALMACENERO |

---

## Como Ejecutar

### Lo que necesitas
- **Docker** (https://docs.docker.com/get-docker/)
- **Docker Compose** (viene incluido con Docker Desktop)

### Paso 1 — Levantar el sistema
```bash
cd sistema-de-autogestion
docker compose up --build
```
Esto construye e inicia 3 contenedores:
- **db:** PostgreSQL 16 (puerto 5432)
- **backend:** Spring Boot (puerto 8080)
- **frontend:** Nginx (puerto 5500)

Espera hasta que veas los logs de los contenedores iniciando.

### Paso 2 — Abrir el navegador
- **Frontend:** http://localhost:5500
- **Backend API:** http://localhost:8080/api

### Paso 3 — Iniciar sesion
- **Email:** `admin@sanmartin.pe`
- **Contrasena:** `admin123`

### Paso 4 — Recorrer el flujo del negocio
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

Al levantar Docker, se insertan automaticamente estos datos:

| Dato | Cantidad |
|------|----------|
| Usuarios | 3 (admin, mecanico, almacenero) |
| Clientes | 3 (Juan Perez, Maria Lopez, Carlos Garcia) |
| Vehiculos | 3 (Toyota Corolla, Hyundai Accent, Nissan Sentra) |
| Servicios | 8 (cambio de aceite, alineacion, frenos, etc.) |
| Productos | 12 (aceite, filtros, pastillas, discos, etc.) |

> Los datos se mantienen en PostgreSQL dentro del contenedor Docker. Si eliminas el volumen `pgdata`, se perderan y se volveran a insertar al reiniciar.

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
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌─────────────┐
│   Frontend  │────>│  Controller  │────>│   Service    │────>│ PostgreSQL  │
│  (HTML/JS)  │ API │  (REST)      │     │ (Logica)     │     │   (DB)      │
└─────────────┘     └──────────────┘     └──────────────┘     └─────────────┘
     HTML               Spring Boot          Spring Boot        Docker
   Bootstrap            @RestController      @Service          Puerto 5432
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

## URLs del Sistema

- Frontend: http://localhost:5500
- Backend API: http://localhost:8080/api

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
Cierra otras aplicaciones que puedan estar usando ese puerto, o cambia el puerto en `docker-compose.yml`:
```yaml
backend:
  ports:
    - "8081:8080"
```

### "Docker no encontrado"
Instala Docker Desktop desde https://docs.docker.com/get-docker/

### "Los contenedores no inician"
Verifica que Docker este corriendo. Puedes revisar los logs con:
```bash
docker compose logs
```

### "Los datos se perdieron"
Si eliminaste el volumen `pgdata`, se pierden los datos. Al reiniciar Docker se vuelven a insertar automaticamente.

### "Recibo 403 o error CORS"
Asegurate de que:
1. El frontend se sirve por HTTP (no `file://`)
2. El backend esta corriendo en el puerto 8080
3. Usas `http://localhost:5500` (no doble clic al HTML)

---

**Desarrollado para el taller mecanico automotriz**
