# Compo — Sistema de Gestión Musical

Aplicación web full stack para gestionar compositores, directores, intérpretes, obras e interpretaciones musicales. Permite registrar, consultar, modificar y eliminar entidades del dominio musical con autenticación segura mediante JWT.

---

## Stack Tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Backend | Spring Boot | 3.2.3 |
| Lenguaje backend | Java | 17 |
| ORM | Spring Data JPA / Hibernate | 6.x |
| Seguridad | Spring Security + JWT (JJWT) | 0.11.5 |
| Documentación API | SpringDoc OpenAPI (Swagger UI) | 2.3.0 |
| Base de datos | PostgreSQL | 16 |
| Frontend | Vue.js | 3.4 |
| Build frontend | Vite | 5.x |
| Router frontend | Vue Router | 4.x |
| Cliente HTTP | Axios | 1.6 |
| Servidor estático | Nginx | Alpine |
| Contenedores | Docker + Docker Compose | 3.9 |
| Build backend | Maven | 3.9 |

---

## Arquitectura de la Solución

```
+------------------------------------------------------------------+
|                         CLIENTE (Navegador)                      |
|                                                                  |
|  +-------------------------------------------------------------+ |
|  |  Vue.js 3  |  Vue Router (history mode)  |  Axios + JWT    | |
|  +-----------------------------+-------------------------------- + |
+-------------------------------|----------------------------------+
                                | HTTP/JSON  +  Authorization: Bearer <token>
                                v
+------------------------------------------------------------------+
|                    Nginx (puerto 80)                             |
|   /          --> sirve archivos estáticos Vue                   |
|   /api       --> proxy_pass http://backend:8080                 |
+-------------------------------|----------------------------------+
                                |
                                v
+------------------------------------------------------------------+
|               Spring Boot 3 API REST (puerto 8080)              |
|                                                                  |
|  +----------------+  +----------------+  +-------------------+  |
|  |  Controllers   |  |   Services     |  |  Repositories     |  |
|  |  (REST Layer)  |->| (Biz Logic)    |->|  (JPA/Hibernate)  |  |
|  +----------------+  +----------------+  +---------+---------+  |
|                                                    |            |
|  Spring Security --> JwtFilter --> UserDetails     |            |
|  Swagger UI en: /swagger-ui.html                   |            |
+----------------------------------------------------+------------+
                                                     | JDBC
                                                     v
                                       +-------------------------+
                                       |   PostgreSQL 16         |
                                       |   (puerto 5432)         |
                                       |   compo_db              |
                                       +-------------------------+
```

### Capas de la aplicación

| Capa | Responsabilidad |
|------|----------------|
| **Presentation** | Controladores REST (`@RestController`), manejo de DTOs, validación de entrada (`@Valid`), mapeo de excepciones a respuestas HTTP |
| **Business** | Servicios (`@Service`), reglas de negocio, lógica de autenticación JWT, gestión de transacciones |
| **Persistence** | Repositorios Spring Data JPA (`@Repository`), entidades JPA (`@Entity`), consultas JPQL |

---

## Estructura del Proyecto

```
compo/
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/compo/
│           │   ├── CompoApplication.java
│           │   ├── config/
│           │   │   ├── SecurityConfig.java
│           │   │   └── SwaggerConfig.java
│           │   ├── controller/
│           │   │   ├── AuthController.java
│           │   │   ├── CompositoresController.java
│           │   │   ├── DirectoresController.java
│           │   │   ├── InterpretesController.java
│           │   │   ├── ObrasController.java
│           │   │   └── InterpretacionesController.java
│           │   ├── dto/
│           │   │   ├── LoginRequest.java
│           │   │   ├── LoginResponse.java
│           │   │   ├── RegisterRequest.java
│           │   │   └── ...
│           │   ├── entity/
│           │   │   ├── Usuario.java
│           │   │   ├── Compositor.java
│           │   │   ├── Director.java
│           │   │   ├── Interprete.java
│           │   │   ├── Obra.java
│           │   │   └── Interpretacion.java
│           │   ├── repository/
│           │   │   ├── UsuarioRepository.java
│           │   │   ├── CompositoresRepository.java
│           │   │   ├── DirectoresRepository.java
│           │   │   ├── InterpretesRepository.java
│           │   │   ├── ObrasRepository.java
│           │   │   └── InterpretacionesRepository.java
│           │   ├── service/
│           │   │   ├── AuthService.java
│           │   │   ├── CompositoresService.java
│           │   │   ├── DirectoresService.java
│           │   │   ├── InterpretesService.java
│           │   │   ├── ObrasService.java
│           │   │   └── InterpretacionesService.java
│           │   └── security/
│           │       ├── JwtUtil.java
│           │       ├── JwtFilter.java
│           │       └── UserDetailsServiceImpl.java
│           └── resources/
│               ├── application.properties
│               └── data.sql
│
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── router/
│       │   └── index.js
│       ├── stores/
│       │   └── auth.js
│       ├── services/
│       │   └── api.js
│       └── views/
│           ├── LoginView.vue
│           ├── DashboardView.vue
│           ├── CompositoresView.vue
│           ├── DirectoresView.vue
│           ├── InterpretesView.vue
│           ├── ObrasView.vue
│           └── InterpretacionesView.vue
│
├── docker-compose.yml
├── README.md
└── postman_collection.json
```

---

## Modelo de Datos

### Tabla: `usuarios`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| cedula | VARCHAR(20) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL (BCrypt) |
| nombre | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | UNIQUE, NOT NULL |
| rol | VARCHAR(10) | NOT NULL (ADMIN / USER) |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### Tabla: `compositores`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| nacionalidad | VARCHAR(100) | NOT NULL |
| fecha_nacimiento | DATE | NOT NULL |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### Tabla: `directores`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| nacionalidad | VARCHAR(100) | NOT NULL |
| fecha_nacimiento | DATE | NOT NULL |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### Tabla: `interpretes`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| tipo_voz_o_instrumento | VARCHAR(100) | NOT NULL |
| nacionalidad | VARCHAR(100) | NOT NULL |
| fecha_nacimiento | DATE | NOT NULL |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### Tabla: `obras`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| titulo | VARCHAR(200) | NOT NULL |
| descripcion | TEXT | |
| genero | VARCHAR(100) | NOT NULL |
| fecha_creacion | DATE | NOT NULL |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### Tabla: `obras_compositores` (ManyToMany)
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| obra_id | BIGINT | FK -> obras(id) |
| compositor_id | BIGINT | FK -> compositores(id) |
| PK compuesta: (obra_id, compositor_id) | | |

### Tabla: `interpretaciones`
| Columna | Tipo | Restricciones |
|---------|------|--------------|
| id | BIGSERIAL | PK |
| obra_id | BIGINT | FK -> obras(id) |
| interprete_id | BIGINT | FK -> interpretes(id) |
| director_id | BIGINT | FK -> directores(id) |
| fecha_interpretacion | DATE | NOT NULL |
| lugar | VARCHAR(200) | NOT NULL |
| observaciones | TEXT | |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

---

## Diagrama Entidad-Relación (ASCII)

```
USUARIOS
  id (PK)
  cedula (UNIQUE)
  password
  nombre
  email (UNIQUE)
  rol
  activo


COMPOSITORES              OBRAS_COMPOSITORES              OBRAS
  id (PK)                  obra_id (PK, FK) ---------->   id (PK)
  nombre         <-------- compositor_id (PK, FK)         titulo
  apellido                                                 descripcion
  nacionalidad                                             genero
  fecha_nacimiento                                         fecha_creacion
  activo                                                   activo
                                                              |
                                                              | (1:N)
                                                              v
INTERPRETES              INTERPRETACIONES              DIRECTORES
  id (PK)  ----------->   id (PK)          <-----------  id (PK)
  nombre                  obra_id (FK)                   nombre
  apellido                interprete_id (FK)             apellido
  tipo_voz_o_instrumento  director_id (FK)               nacionalidad
  nacionalidad            fecha_interpretacion           fecha_nacimiento
  fecha_nacimiento        lugar                          activo
  activo                  observaciones
                          activo
```

---

## Endpoints de la API

### Autenticación (`/api/auth`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | Iniciar sesión, retorna JWT | No |
| POST | `/api/auth/register` | Registrar nuevo usuario | No |

### Compositores (`/api/compositores`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| GET | `/api/compositores` | Listar todos los compositores activos | Si |
| GET | `/api/compositores/{id}` | Obtener compositor por ID | Si |
| POST | `/api/compositores` | Crear nuevo compositor | Si (ADMIN) |
| PUT | `/api/compositores/{id}` | Actualizar compositor | Si (ADMIN) |
| DELETE | `/api/compositores/{id}` | Eliminar compositor (soft delete) | Si (ADMIN) |

### Directores (`/api/directores`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| GET | `/api/directores` | Listar todos los directores activos | Si |
| GET | `/api/directores/{id}` | Obtener director por ID | Si |
| POST | `/api/directores` | Crear nuevo director | Si (ADMIN) |
| PUT | `/api/directores/{id}` | Actualizar director | Si (ADMIN) |
| DELETE | `/api/directores/{id}` | Eliminar director (soft delete) | Si (ADMIN) |

### Intérpretes (`/api/interpretes`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| GET | `/api/interpretes` | Listar todos los intérpretes activos | Si |
| GET | `/api/interpretes/{id}` | Obtener intérprete por ID | Si |
| POST | `/api/interpretes` | Crear nuevo intérprete | Si (ADMIN) |
| PUT | `/api/interpretes/{id}` | Actualizar intérprete | Si (ADMIN) |
| DELETE | `/api/interpretes/{id}` | Eliminar intérprete (soft delete) | Si (ADMIN) |

### Obras (`/api/obras`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| GET | `/api/obras` | Listar todas las obras activas | Si |
| GET | `/api/obras/{id}` | Obtener obra por ID (incluye compositores) | Si |
| POST | `/api/obras` | Crear obra (con lista de compositorIds) | Si (ADMIN) |
| PUT | `/api/obras/{id}` | Actualizar obra y su lista de compositores | Si (ADMIN) |
| DELETE | `/api/obras/{id}` | Eliminar obra (soft delete) | Si (ADMIN) |

### Interpretaciones (`/api/interpretaciones`)
| Método | Endpoint | Descripción | Auth Requerida |
|--------|----------|-------------|----------------|
| GET | `/api/interpretaciones` | Listar todas las interpretaciones activas | Si |
| GET | `/api/interpretaciones/{id}` | Obtener interpretación por ID | Si |
| GET | `/api/interpretaciones/interprete/{id}` | Filtrar por intérprete | Si |
| GET | `/api/interpretaciones/director/{id}` | Filtrar por director | Si |
| POST | `/api/interpretaciones` | Registrar nueva interpretación | Si (ADMIN) |
| PUT | `/api/interpretaciones/{id}` | Actualizar interpretación | Si (ADMIN) |
| DELETE | `/api/interpretaciones/{id}` | Eliminar interpretación (soft delete) | Si (ADMIN) |

---

## Flujo de Autenticación

```
1. Cliente envía POST /api/auth/login
   Body: { "cedula": "1000000001", "password": "Admin123" }

2. Spring Security delega a AuthService
   --> Verifica cedula en la base de datos
   --> Compara contraseña con BCrypt
   --> Si válida, genera JWT firmado (HS256) con expiración de 24 h

3. Servidor responde:
   { "token": "eyJhbGciOiJIUzI1NiJ9...", "tipo": "Bearer", "rol": "ADMIN" }

4. Cliente almacena el token en localStorage

5. Para cada petición protegida:
   Request Header: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

6. JwtFilter intercepta cada request:
   --> Extrae y valida el token (firma + expiración)
   --> Carga UserDetails desde la base de datos
   --> Establece SecurityContext

7. El controlador procesa la petición con el usuario autenticado en contexto
```

---

## Instrucciones de Instalación

### Prerequisitos

| Herramienta | Versión mínima | Verificar |
|-------------|---------------|-----------|
| Java | 17+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Node.js | 20+ | `node -version` |
| PostgreSQL | 16+ | `psql --version` |
| Docker | 24+ | `docker -version` |
| Docker Compose | 2.x | `docker compose version` |

---

### Opción A: Ejecución Manual (sin Docker)

#### 1. Base de datos PostgreSQL (manual)

```sql
CREATE DATABASE compo_db;
CREATE USER compo_user WITH PASSWORD 'compo_pass';
GRANT ALL PRIVILEGES ON DATABASE compo_db TO compo_user;
```

#### 2. Ejecutar Backend

```bash
cd backend
mvn spring-boot:run
# Disponible en: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

#### 3. Ejecutar Frontend

```bash
cd frontend
npm install
npm run dev
# Disponible en: http://localhost:5173
```

> El proxy de Vite redirige automáticamente `/api/*` hacia `http://localhost:8080`.

---

### Opción B: Ejecutar con Docker Compose

```bash
docker-compose up -d
# Frontend: http://localhost
# Backend:  http://localhost:8080
# Swagger:  http://localhost:8080/swagger-ui.html
```

Otros comandos utiles:

```bash
# Ver logs en tiempo real
docker-compose logs -f

# Detener todos los servicios
docker-compose down

# Detener y eliminar la base de datos persistida
docker-compose down -v

# Reconstruir solo el backend
docker-compose up -d --build backend
```

URLs disponibles con Docker Compose:

| Servicio | URL |
|---------|-----|
| Frontend | `http://localhost` |
| Backend API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| PostgreSQL | `localhost:5432` |

---

## Usuarios de Prueba

| Cedula | Contraseña | Rol |
|--------|-----------|-----|
| `1000000001` | `Admin123` | ADMIN |
| `1000000002` | `User123` | USER |

> Las contraseñas están almacenadas con hash BCrypt (costo 10) en la base de datos.

---

## Datos de Prueba Precargados

El archivo `data.sql` incluye datos iniciales cargados automáticamente al arrancar:

**Compositores:** Ludwig van Beethoven, Wolfgang Amadeus Mozart, Johann Sebastian Bach

**Directores:** Herbert von Karajan, Carlos Kleiber

**Interpretes:** Placido Domingo (Tenor), Martha Argerich (Piano), Itzhak Perlman (Violin)

**Obras:**
- Sinfonia N 9 en Re menor (compositor: Beethoven)
- Requiem en Re menor K. 626 (compositores: Mozart + Bach — ejemplo ManyToMany)

**Interpretaciones:** 3 registros de ejemplo con fechas, lugares y observaciones reales

---

## Evidencia de Funcionamiento

Funcionalidades verificables en la aplicación:

- [ ] Login con cedula y contraseña / cierre de sesion
- [ ] Token JWT almacenado en localStorage y enviado automaticamente en cada request
- [ ] Listado de compositores, directores, interpretes, obras e interpretaciones
- [ ] Creacion de nuevas entidades desde el formulario (rol ADMIN)
- [ ] Edicion de entidades existentes
- [ ] Eliminacion logica (soft delete, campo `activo = false`)
- [ ] Filtrado de interpretaciones por interprete y por director
- [ ] Relacion ManyToMany Obra-Compositor visible en el detalle de una obra
- [ ] Swagger UI con todos los endpoints documentados y ejecutables
- [ ] Datos de prueba cargados automaticamente al iniciar

---

## Relaciones Principales

### ManyToMany: Obra — Compositor

Una obra puede tener uno o varios compositores, y un compositor puede haber creado varias obras. Esta relacion se gestiona mediante la tabla intermedia `obras_compositores`. Al crear o actualizar una obra, se pasa una lista de `compositorIds` en el cuerpo del request, y el backend resuelve las entidades y actualiza la tabla intermedia.

### Interpretacion: el corazon del dominio

La entidad `Interpretacion` representa un evento musical concreto: una obra especifica, interpretada por un interprete especifico, bajo la direccion de un director especifico, en una fecha y lugar determinados. Esta entidad conecta todas las entidades del dominio y permite consultas cruzadas del tipo "todas las obras que interpreto Martha Argerich" o "todos los directores que dirigieron obras de Beethoven".

---

## Variables de Entorno (Docker)

| Variable | Valor por defecto | Descripcion |
|----------|------------------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/compo_db` | URL JDBC hacia PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `compo_user` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | `compo_pass` | Contraseña de la base de datos |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Estrategia DDL de Hibernate |
| `JWT_SECRET` | `compoSecretKeyForJWTTokenGeneration2024VeryLong` | Clave secreta de firma JWT |
| `JWT_EXPIRATION` | `86400000` | Expiracion del token en ms (24 h) |

---

## Comandos Utiles

```bash
# Ver estado de los contenedores
docker-compose ps

# Acceder a la base de datos desde terminal
docker exec -it compo_postgres psql -U compo_user -d compo_db

# Ver logs del backend solamente
docker-compose logs -f backend

# Limpiar todo (contenedores + volúmenes + imagenes locales)
docker-compose down -v --rmi local
```

