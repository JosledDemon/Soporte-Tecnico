# 🧩 Sistema de Soporte Técnico – CRUD de Solicitudes

## 📖 Descripción general
Este proyecto es una **API REST** desarrollada en **Spring Boot** que permite gestionar solicitudes de soporte técnico mediante operaciones CRUD (**Crear**, **Leer**, **Actualizar**, **Eliminar**).  

El sistema usa una **arquitectura en capas (Controller, Service, Mapper, DTO, Model)** y mantiene los datos **en memoria con `HashMap`**, ideal para **aprendizaje, pruebas o demostraciones académicas**.

---

## 🏗️ Arquitectura del proyecto

| Capa | Descripción | Paquete |
|------|--------------|----------|
| **Controller** | Expone los endpoints HTTP (API REST). | `com.empresa.soporte_tecnico.controller` 
|
| **Service** | Contiene la lógica del negocio. | `com.empresa.soporte_tecnico.service` 
|
| **Mapper** | Convierte entre entidades (`model`) y DTOs (`dto`). | `com.empresa.soporte_tecnico.mapper` 
|
| **DTO (Data Transfer Object)** | Objetos utilizados para comunicación con el frontend o API |`com.empresa.soporte_tecnico.dto` 
|
| **Model** | Define las entidades principales (`Cliente`, `Técnico`, `Solicitud`). |`com.empresa.soporte_tecnico.model` 
|
| **Config** | Contiene configuraciones globales como Swagger. | `com.empresa.soporte_tecnico.config`
|
| **Repository** | Interfaces para gestionar la persistencia de entidades y CRUD automático. | `com.empresa.soporte_tecnico.repository` 
|
| **Exception** | Manejo centralizado de errores y validaciones de la API. | `com.empresa.soporte_tecnico.exception` 

---

## ⚙️ Requisitos técnicos

- **Java:** 21 o superior  
- **Spring Boot:** 3.x  
- **Dependencias:**  
  - `spring-boot-starter-web`  
  - `spring-boot-starter-validation`  
  - `springdoc-openapi-starter-webmvc-ui` (para Swagger UI)  

---

## ▶️ Ejecución del proyecto

### Clonar el repositorio
```bash
git clone https://github.com/JosledD/soporte-tecnico.git
cd soporte-tecnico
```

### Compilar y construir
```bash
mvn clean install
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### Acceder en el navegador
👉 [http://localhost:8080/solicitudes](http://localhost:8080/solicitudes)

---

## 🧾 Entidades principales

### 🧍 Cliente
```java
public class Cliente {
    private Long id;
    private String nombre;
    private String correo;
}
```

### 🧑‍🔧 Técnico
```java
public class Tecnico {
    private Long id;
    private String nombre;
    private String especialidad;
}
```

### 🧩 Solicitud
```java
public class Solicitud {
    private Long id;
    private String descripcion;
    private Cliente cliente;
    private Tecnico tecnicoAsignado;
    private String estado;
}
```

---

## 📦 DTOs (Data Transfer Objects)

Se utilizan para intercambiar información con el frontend, evitando exponer directamente las entidades del modelo.

```java
public class ClienteDto {
    private Long id;
    private String nombre;
    private String correo;
}

public class TecnicoDto {
    private Long id;
    private String nombre;
    private String especialidad;
}

public class SolicitudDto {
    private Long id;
    private String descripcion;
    private ClienteDto cliente;
    private TecnicoDto tecnicoAsignado;
    private String estado;
}
```

📌 Ejemplo de JSON esperado al crear una solicitud:
```json
{
  "descripcion": "El sistema no arranca correctamente",
  "cliente": { "id": 1 },
  "tecnicoAsignado": { "id": 10 },
  "estado": "Pendiente"
}
```

---

## 🔄 Mappers

Los mappers convierten entre entidades (`model`) y DTOs (`dto`).

### 🔹 Ejemplo de `SolicitudMapper`
```java
public class SolicitudMapper {

    public static SolicitudDto toDto(Solicitud solicitud) {
        if (solicitud == null) return null;

        return SolicitudDto.builder()
                .id(solicitud.getId())
                .descripcion(solicitud.getDescripcion())
                .cliente(ClienteMapper.toDto(solicitud.getCliente()))
                .tecnicoAsignado(TecnicoMapper.toDto(solicitud.getTecnicoAsignado()))
                .estado(solicitud.getEstado())
                .build();
    }

    public static Solicitud toEntity(SolicitudDto dto) {
        if (dto == null) return null;

        Solicitud solicitud = new Solicitud();
        solicitud.setId(dto.getId());
        solicitud.setDescripcion(dto.getDescripcion());

        if (dto.getCliente() != null && dto.getCliente().getId() != null)
            solicitud.setCliente(new Cliente(dto.getCliente().getId()));

        if (dto.getTecnicoAsignado() != null && dto.getTecnicoAsignado().getId() != null)
            solicitud.setTecnicoAsignado(new Tecnico(dto.getTecnicoAsignado().getId()));

        solicitud.setEstado(dto.getEstado());
        return solicitud;
    }
}
```

---

## 💡 Lógica de negocio – Service

### Interfaz
```java
public interface SolicitudService {
    Solicitud crearSolicitud(Solicitud solicitud);
    List<Solicitud> obtenerSolicitudes();
    Solicitud obtenerPorId(Long id);
    Solicitud actualizarSolicitud(Long id, Solicitud solicitud);
    void eliminarSolicitud(Long id);
}
```

### Implementación
```java
@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final Map<Long, Solicitud> solicitudes = new HashMap<>();
    private Long contador = 1L;

    @Override
    public Solicitud crearSolicitud(Solicitud solicitud) {
        solicitud.setId(contador++);
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public List<Solicitud> obtenerSolicitudes() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return solicitudes.get(id);
    }

    @Override
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitud) {
        if (!solicitudes.containsKey(id)) return null;
        solicitud.setId(id);
        solicitudes.put(id, solicitud);
        return solicitud;
    }

    @Override
    public void eliminarSolicitud(Long id) {
        solicitudes.remove(id);
    }
}
```

---

## 🌐 Controlador REST

```java
@RestController
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de soporte técnico")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudDto> crearSolicitud(@Valid @RequestBody SolicitudDto solicitudDto) {
        Solicitud solicitud = SolicitudMapper.toEntity(solicitudDto);
        Solicitud creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(SolicitudMapper.toDto(creada));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDto>> obtenerSolicitudes() {
        List<SolicitudDto> lista = solicitudService.obtenerSolicitudes()
                .stream()
                .map(SolicitudMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto> obtenerSolicitudPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        return solicitud != null
                ? ResponseEntity.ok(SolicitudMapper.toDto(solicitud))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDto> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudDto solicitudDto) {
        Solicitud solicitud = SolicitudMapper.toEntity(solicitudDto);
        Solicitud actualizada = solicitudService.actualizarSolicitud(id, solicitud);
        return actualizada != null
                ? ResponseEntity.ok(SolicitudMapper.toDto(actualizada))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 📘 Swagger (OpenAPI)

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI soporteTecnicoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Soporte Técnico")
                        .version("1.0")
                        .description("Documentación del CRUD de Solicitudes, Clientes y Técnicos (usando DTOs)")
                        .contact(new Contact()
                                .name("Departamento de Sistemas")
                                .email("soporte@empresa.com")));
    }
}
```

📍 Acceso Swagger UI:  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧱 Estructura del proyecto

```
src/main/java/com/empresa/soporte_tecnico
│
├── config/
│   └── SwaggerConfig.java
│
├── controller/
│   ├── ClienteController.java
│   ├── SolicitudController.java
│   └── TecnicoController.java
│
├── dto/
│   ├── ClienteDto.java
│   ├── SolicitudDto.java
│   └── TecnicoDto.java
│
├── exception/
│   └── GlobalExceptionHandler.java
│
├── mapper/
│   ├── ClienteMapper.java
│   ├── SolicitudMapper.java
│   └── TecnicoMapper.java
│
├── model/
│   ├── Cliente.java
│   ├── Solicitud.java
│   └── Tecnico.java
│
├── repository/
│   ├── ClienteRepository.java
│   ├── SolicitudRepository.java
│   └── TecnicoRepository.java
│
│
└── service/
    ├── SolicitudService.java
    └── SolicitudServiceImpl.java
```

---

## 🧪 Flujo de funcionamiento

1. El cliente envía la solicitud HTTP (via Postman o Swagger).  
2. El **Controller** recibe la petición y la convierte con el **Mapper**.  
3. El **Service** gestiona los datos en memoria usando un `HashMap`.  
4. La respuesta se devuelve como un **DTO en formato JSON**.  

---

## ✅ Conclusión

✔ CRUD 100% funcional y en memoria  
✔ Arquitectura limpia y escalable  
✔ Mapeo completo entre entidades y DTOs  
✔ Documentación interactiva con Swagger UI  
✔ Ideal para aprendizaje, prácticas y demostraciones académicas  

---

## 👨‍💻 Integrantes

- Velarde Robles Francisco Xavier Leon  
- Roman Huaman Josled Luis Antonio  
- Peña Chavez Gissel Melani  
- Osorio Guzman Jose Luis  
- Colina Martin Jesús Gabriel  

🗓️ **Última actualización:** 10 de noviembre de 2025

