# Autos Caribe Web

Proyecto universitario desarrollado con Java, Spring Boot, Thymeleaf, Spring Data JPA y MySQL.

## Avance 2

En este avance se integraron los módulos principales del sistema:

- Gestión de usuarios y roles.
- Inicio de sesión y registro.
- CRUD de vehículos.
- Catálogo de vehículos.
- Detalle de cada vehículo.
- Búsqueda por marca.
- Filtro por categoría.
- Filtro por rango de precio.
- Registro y visualización de solicitudes.
- Integración entre catálogo, vehículos, usuarios y solicitudes.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL
- Bootstrap
- Maven
- NetBeans
- Git y GitHub

## División del proyecto

### Integrante 1 - Seguridad y usuarios

Historias de usuario:

- HU-01 Registro de usuario.
- HU-02 Inicio de sesión.
- HU-16 Gestionar usuarios.
- HU-17 Gestionar roles.

Incluye entidades, repositorios, servicios, controladores, vistas Thymeleaf, login, registro y asignación de roles.

Rama:

```text
feature-security
```

### Integrante 2 - Gestión de vehículos

Historias de usuario:

- HU-11 Registrar vehículos.
- HU-12 Editar vehículos.
- HU-13 Eliminar vehículos.

Incluye la entidad Vehiculo, repositorio, servicio, controlador, listado, formulario, edición y eliminación.

Rama:

```text
feature-vehiculos
```

### Integrante 3 - Catálogo de vehículos

Historias de usuario:

- HU-03 Visualizar catálogo.
- HU-04 Ver detalle del vehículo.
- HU-05 Buscar por marca.
- HU-06 Filtrar por categoría.
- HU-07 Filtrar por precio.

Incluye página principal, catálogo con tarjetas Bootstrap, vista de detalle, barra de búsqueda, filtros, imágenes y enlace para enviar solicitudes.

Rama:

```text
feature-catalogo
```

### Integrante 4 - Solicitudes e integración

Historias de usuario:

- HU-08 Enviar solicitud.
- HU-15 Visualizar solicitudes.

Incluye entidad Solicitud, repositorio, servicio, controlador, formulario, listado, relaciones entre cliente, vendedor y vehículo, navbar, footer y fragmentos Thymeleaf.

Rama:

```text
feature-solicitudes
```

## Consultas utilizadas

El proyecto utiliza solamente consultas derivadas de Spring Data JPA.

No se utilizaron consultas JPQL ni SQL nativo en los repositorios.

Ejemplos utilizados en `VehiculoRepository`:

```java
List<Vehiculo> findByMarcaContainingIgnoreCaseOrderByMarcaAsc(String marca);

List<Vehiculo> findByCategoriaIgnoreCaseOrderByMarcaAsc(String categoria);

List<Vehiculo> findByPrecioBetweenOrderByPrecioAsc(
        BigDecimal precioMinimo,
        BigDecimal precioMaximo);
```

### Explicación de la consulta por marca

```java
findByMarcaContainingIgnoreCaseOrderByMarcaAsc
```

- `findBy`: indica que se realizará una búsqueda.
- `Marca`: es el atributo de la entidad Vehiculo.
- `Containing`: permite coincidencias parciales.
- `IgnoreCase`: ignora mayúsculas y minúsculas.
- `OrderByMarcaAsc`: ordena los resultados por marca de forma ascendente.

Ejemplo: si el usuario escribe `Toy`, puede encontrar `Toyota`.

## Flujo del catálogo

```text
catalogo.html
        ↓
CatalogoController
        ↓
VehiculoService
        ↓
VehiculoRepository
        ↓
MySQL
```

El formulario envía los datos al controlador, el controlador llama al servicio y el servicio utiliza el repositorio para consultar la base de datos.

## Rutas principales

```text
/
/catalogo
/catalogo/buscar
/catalogo/categoria
/catalogo/precio
/catalogo/detalle/{idVehiculo}
/vehiculo/listado
/registro
/login
```

## Base de datos

La base de datos utilizada es:

```text
autoscaribe_db
```

Tablas principales:

- usuario
- rol
- usuario_rol
- vehiculo
- solicitud

## Configuración de MySQL

Revisar el archivo:

```text
src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/autoscaribe_db
spring.datasource.username=root
spring.datasource.password=CONTRASENA_MYSQL
spring.jpa.hibernate.ddl-auto=update
```

Cada integrante debe colocar su propia contraseña de MySQL.

## Cómo ejecutar el proyecto

1. Importar el archivo SQL en MySQL Workbench.
2. Abrir el proyecto en NetBeans.
3. Revisar la contraseña de MySQL en `application.properties`.
4. Ejecutar `Clean and Build`.
5. Ejecutar el proyecto.
6. Abrir `http://localhost:8080` en el navegador.

## Imágenes

Las imágenes se guardan en la columna `ruta_imagen`.

Pueden utilizarse rutas locales:

```text
/images/vehiculo.jpg
```

o enlaces directos de Internet:

```text
https://sitio.com/vehiculo.jpg
```

## GitHub

Flujo recomendado:

1. Clonar el repositorio.
2. Cambiarse a la rama correspondiente.
3. Realizar los cambios.
4. Hacer Commit.
5. Hacer Push.
6. Crear un Pull Request para integrar los cambios.

Ejemplo de mensaje de commit:

```text
Implementación del catálogo y consultas derivadas
```

## Estado actual

El proyecto permite mostrar el catálogo, buscar por marca, filtrar por categoría y precio, ver el detalle de un vehículo, administrar vehículos, registrar usuarios, iniciar sesión, enviar solicitudes y visualizar solicitudes.

