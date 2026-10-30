# ProductosJdbc · LogísticaApp

Proyecto base de la práctica **UT02: Spring Boot + JDBC** para gestionar productos y fabricantes.

- **Java:** 25
- **Spring Boot:** 4.1.2
- **Build:** Maven (incluye `mvnw`)
- **Paquete base:** `es.daw.productosjdbc`
- **Base de datos:** H2 en fichero (`jdbc:h2:file:./data/logisticadb`)

## Qué incluye

- Proyecto Maven generado desde Spring Initializr.
- Dependencias: Spring Web (MVC), Thymeleaf, Validation, JDBC API, H2, DevTools y Lombok.
- `application.properties` con la configuración de H2 y la consola `/h2-console`.
- `schema.sql` y `data.sql` con las tablas, la FK y los datos de prueba.
- Modelo: `Fabricante` y `Producto` (con `Fabricante` dentro, no el `int` de la FK).
- Excepciones propias y `GlobalExceptionHandler`.
- Configuración MVC para la portada (`WebConfig`).
- Vistas Thymeleaf y estilos CSS (portada, listados, formularios y página de error).

## Capas de la práctica

El proyecto base no traía estas capas; son las que hay que implementar o
completar:

- `repository/` (interfaces e implementaciones JDBC)
- `service/`
- `controller/`
- `dto/`

Mientras no estén terminadas y compilando, `/productos` y `/fabricantes` no
funcionarán aunque la base de datos y las vistas estén preparadas.

## Cómo arrancar

```bash
cd /home/alumnot/Escritorio/aprenderDaw/ProductosJdbc
./mvnw spring-boot:run
```

Después:

- Portada: <http://localhost:8080/>
- Consola H2: <http://localhost:8080/h2-console>
    - JDBC URL: `jdbc:h2:file:./data/logisticadb;AUTO_SERVER=TRUE`
    - User: `sa`
    - Password: vacía

La base de datos se guarda en `data/logisticadb.mv.db` dentro del proyecto. Al
arrancar, `schema.sql` vuelve a crear las tablas y `data.sql` inserta los datos
de prueba. Para empezar de cero, para la aplicación y borra la carpeta `data/`.

## Ver la base de datos desde IntelliJ

El proyecto ya incluye una fuente de datos H2 llamada `default`, configurada
con la misma URL que la aplicación. En la ventana **Database**:

1. Pulsa **Refresh** para sincronizar.
2. Abre el esquema `PUBLIC`: deberían aparecer `FABRICANTE` y `PRODUCTO`.
3. Los ficheros `schema.sql` y `data.sql` ya están asociados a esa fuente de datos.

## Comprobar la base de datos

En la consola H2:

```sql
SELECT p.codigo, p.nombre, p.precio, f.nombre AS fabricante
FROM producto p
JOIN fabricante f ON p.codigo_fabricante = f.codigo
ORDER BY p.codigo;
```

## Orden recomendado para completar la práctica

1. `repository/GenericRepository`, `ProductoRepository` y `FabricanteRepository`.
2. `repository/ProductoRepositoryJdbc` y `repository/FabricanteRepositoryJdbc`.
3. `service/ProductoService` y `service/FabricanteService`.
4. `dto/ProductoForm`.
5. `controller/ProductoController` y `controller/FabricanteController`.
6. Probar el CRUD completo y la regla de negocio de la FK.
