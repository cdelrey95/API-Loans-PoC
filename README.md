# API Loans - Proof of Concept

## 1. Instrucciones para ejecutar el proyecto

Este proyecto es una **POC (Proof of Concept)** desarrollada con **Spring Boot 3**, **Java 21** y **Maven**, que expone una API REST para la gestión de solicitudes de préstamos (*loans*), incluyendo autenticación básica mediante **Spring Security**, persistencia con **JPA/Hibernate** y base de datos **H2 en memoria**.

### Requisitos previos

- Java **JDK 21**
- Maven **+3.9.x**
- Bash (Git Bash en Windows)

### Ejecución usando `build.sh` (recomendado)

El proyecto incluye un script `build.sh` con una **configuración por defecto** de Java y Maven. 
Es necesario actualizar las variables **JAVA_HOME** y **MAVEN_HOME** según el entorno local:

```bash
JAVA_HOME="/c/dev/tools/Java/jdk-21.0.7"
MAVEN_HOME="/c/dev/tools/Maven/apache-maven-3.9.6"
```
Este script:
1. Configura las variables de entorno `JAVA_HOME` y `MAVEN_HOME`.
2. Añade ambos al `PATH`.
3. Ejecuta:
```bash
mvn clean install
```

Una vez complilado el proyecto, se puede ejecutar la aplicación con:
```bash
java -jar target/poc-1.0.0.jar
``` 
o con Maven:
```bash
mvn spring-boot:run
```

### Accesos

- **Swagger UI**:  
  http://localhost:8080/api-loans/swagger-ui.html

- **OpenAPI Docs**:  
  http://localhost:8080/api-loans/v3/api-docs

- **H2 Console**:  
  http://localhost:8080/h2-console

  * JDBC URL: `jdbc:h2:mem:loandb`
  * username: `user`  
  * password: (vacío)

### Usuarios de prueba

| Usuario | Password | Roles |
|--------|----------|-------|
| admin  | admin    | ROLE_ADMIN, ROLE_USER |
| user   | user     | ROLE_USER |


---

## 2. Arquitectura y decisiones técnicas

El proyecto sigue una arquitectura por capas, lo que permite separar responsabilidades y centraliza la lógica de negocio en el servicio.
Los endpoints REST utilizan DTOs con validaciones para desacoplar la API del modelo interno.

La lógica de negocio se concentra en la capa de servicio, donde se controlan las reglas y transiciones de estado de las solicitudes de préstamo. La persistencia se implementa con Spring Data JPA sobre una base de datos H2 en memoria.

Se utiliza MapStruct para el mapeo entre entidades y DTOs y un manejador global de excepciones para centralizar errores. La seguridad se implementa con Spring Security, usando autenticación por formulario y usuarios en memoria con roles. La API se documenta con Springdoc OpenAPI.

Por último, se incluyen tests unitarios y de integración para asegurar la calidad del código.

---

## 3. Funcionalidad

El proyecto simula una API REST para la gestión de solicitudes de préstamos, en la cual se pueden realizar las siguientes operaciones:
- Crear una solicitud de préstamo.
- Consultar una solicitud por su ID.
- Listar todas las solicitudes.
- Actualizar el estado de una solicitud.

Al inicializar la aplicación podemos elegir entre dos usuarios de prueba: `admin` y `user`, siendo `admin`el único con 
acceso a todas las operaciones, mientras que `user` solo puede crear y consultar solicitudes por su ID.

---

## 4. Mejoras y extensiones futuras

- Sustitución de BBDD por MySQL o PostgreSQL.
- Nuevos endpoints a implementar:
    - Borrado lógico de préstamos.
    - Búsqueda avanzada (por estado, fecha, importe, usuario, DNI...).
    - Alta/Baja/Modificación de usuarios.
- Historial de cambios de estado de un préstamo.
- Implementación de eventos asíncronos (EJ: notificaciones por email, cambio de estado...).
- Mayor cobertura de tests (EJ: tests de casos de error).
- Autenticación JWT / OAuth2 en lugar de sesión.
- Inclusión de observabilidad (logs, métricas...).
- Generación de OpenApi en formato YAML.

---

### Autor
Carlos del Rey Pagador
