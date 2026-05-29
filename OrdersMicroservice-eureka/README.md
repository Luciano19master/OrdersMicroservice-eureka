# OrdersMicroservice - Documentación

## Descripción General

OrdersMicroservice es un microservicio Spring Boot encargado de gestionar las órdenes de compra en la aplicación. Implementa funcionalidades de creación, modificación, consulta y eliminación de órdenes, así como la gestión de líneas de ordenes y estados.

## Características Principales

- **Gestión de Órdenes**: Crear, actualizar, obtener y eliminar órdenes
- **Líneas de Orden**: Agregar o eliminar ítems de una orden
- **Estados de Orden**: PENDING → CONFIRMED → SHIPPED → DELIVERED (o CANCELLED)
- **Validación de Libros**: Integración con el microservicio Catálogo para validar disponibilidad
- **Historial de Estados**: Log completo de cambios de estado
- **Cálculo de Totales**: Automático con support para descuentos e impuestos
- **Recupero de Órdenes por Cliente**: Para perfiles de usuario

## Estructura del Proyecto

```
OrdersMicroservice-eureka/
├── src/main/java/com/mircroservice/unir/orders/
│   ├── config/                    # Configuración (BD, RestTemplate)
│   ├── controller/                # Controladores REST
│   │   ├── OrdersController.java
│   │   └── model/                 # DTOs
│   ├── dto/                       # Mappers
│   ├── exception/                 # Excepciones personalizadas
│   ├── repository/                # Interfaces de persistencia
│   │   └── model/                 # Entidades JPA
│   ├── service/                   # Lógica de negocio
│   └── OrdersMicroserviceApplication.java
├── src/main/resources/
│   ├── application.yaml           # Configuración Spring
│   ├── database/
│   │   ├── schema.sql             # Esquema BD
│   │   ├── data.sql               # Datos de prueba
│   │   └── orders.db              # Base de datos SQLite
├── pom.xml                        # Dependencias Maven
└── README.md

```

## Base de Datos

La BD SQLite se crea automáticamente en `src/main/resources/database/orders.db` con las siguientes tablas:

- **orders**: Órdenes principales
- **order_lines**: Líneas de cada orden (items)
- **order_status_log**: Historial de cambios de estado

Incluye datos de prueba con 4 órdenes de ejemplo.

## Endpoints de la API

### GET - Obtener Órdenes

```
GET /api/orders/{orderId}
- Obtiene una orden por ID

GET /api/orders/number/{orderNumber}
- Obtiene una orden por número de orden

GET /api/orders/customer/{customerId}
- Obtiene todas las órdenes de un cliente (ordenadas por fecha descendente)
```

### POST - Crear y Modificar

```
POST /api/orders
- Crea una nueva orden
- Body: OrderDto con datos de facturación

POST /api/orders/{orderId}/lines
- Agrega una línea a una orden
- Body: OrderLineDto

POST /api/orders/{orderId}/confirm
- Cambia estado de PENDING a CONFIRMED

POST /api/orders/{orderId}/ship
- Cambia estado de CONFIRMED a SHIPPED

POST /api/orders/{orderId}/deliver
- Cambia estado de SHIPPED a DELIVERED
```

### PUT - Actualizar

```
PUT /api/orders/{orderId}/billing
- Actualiza la información de facturación
- Body: OrderDto con solo los campos a actualizar
```

### PATCH - Modificaciones Parciales

```
PATCH /api/orders/{orderId}/lines/{lineId}/quantity?quantity=5
- Actualiza la cantidad de una línea

PATCH /api/orders/{orderId}/lines/{lineId}/discount?discountPct=10
- Actualiza el descuento de una línea (porcentaje 0-100)
```

### DELETE - Eliminar

```
DELETE /api/orders/{orderId}/lines/{lineId}
- Elimina una línea de la orden

DELETE /api/orders/{orderId}/cancel
- Cancela la orden (disponible en estados PENDING o CONFIRMED)

DELETE /api/orders/{orderId}
- Elimina completamente la orden (solo si está en PENDING o CANCELLED)
```

## Modelos de Datos

### OrderDto
```json
{
  "id": 1,
  "order_number": "ORD-2024-001",
  "customer_id": 1,
  "status": "PENDING",
  "created_at": "2024-05-29T10:30:00",
  "updated_at": "2024-05-29T10:30:00",
  "shipped_at": null,
  "delivered_at": null,
  "billing_name": "Juan García López",
  "billing_nif": "12345678A",
  "billing_address": "Calle Principal 123",
  "billing_city": "Madrid",
  "billing_zip": "28001",
  "billing_country": "España",
  "subtotal": 49.98,
  "discount": 0.00,
  "tax_rate": 0.21,
  "tax_amount": 10.50,
  "total": 60.48,
  "notes": "Primera orden del cliente",
  "lines": [
    {
      "id": 1,
      "order_id": 1,
      "book_id": 1,
      "quantity": 2,
      "unit_price": 24.99,
      "discount_pct": 0.00,
      "line_total": 49.98
    }
  ],
  "status_log": [
    {
      "id": 1,
      "order_id": 1,
      "from_status": "NONE",
      "to_status": "PENDING",
      "changed_at": "2024-05-29T10:30:00",
      "changed_by": "SYSTEM"
    }
  ]
}
```

## Configuración

### application.yaml

```yaml
server:
  port: 8082

spring:
  application:
    name: OrdersMicroservice
  datasource:
    url: jdbc:sqlite:src/main/resources/database/orders.db
    driver-class-name: org.sqlite.JDBC

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: unir.orders-service
```

## Validaciones

- **Orden Number**: Único en el sistema
- **Customer ID**: Requerido
- **Billing Name**: Requerido
- **Líneas de Orden**: Deben validarse con Catálogo (libro debe existir y estar visible)
- **Cantidad**: Mínimo 1
- **Estados**: Solo transiciones válidas (PENDING → CONFIRMED → SHIPPED → DELIVERED)

## Integración con Catálogo Microservice

El OrdersMicroservice realiza llamadas HTTP al Catálogo para validar que los libros existen y están disponibles:

```java
GET http://catalogue/api/books/{id}/validate
```

Usa el nombre del servicio "catalogue" (registrado en Eureka) en lugar de IP/puerto.

## Manejo de Errores

La API retorna códigos HTTP estándar:

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado
- **204 No Content**: Operación exitosa sin contenido
- **400 Bad Request**: Datos inválidos
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (ej: libro no disponible)
- **500 Internal Server Error**: Error del servidor

Ejemplo de respuesta de error:
```json
{
  "timestamp": "2024-05-29T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Orden con id 999 no encontrada",
  "path": "/api/orders/999"
}
```

## Dependencias Principales

- Spring Boot 3.2.5
- Spring Cloud 2023.0.1
- Spring Data JPA
- Eureka Client
- Lombok
- SQLite JDBC
- Hibernate Community Dialects

## Compilación y Ejecución

### Compilar
```bash
mvn clean compile
```

### Empaquetar
```bash
mvn package -DskipTests
```

### Ejecutar
```bash
mvn spring-boot:run
```

O si ya compilaste:
```bash
java -jar target/orders-service-1.0.0.jar
```

## Requiere

- Eureka Server corriendo en `http://localhost:8761`
- Catálogo Microservice corriendo en puerto 8081
- Java 17 o superior
- Maven 3.6 o superior

## Desarrollo Futura

- [ ] Agregar paginación en GetOrdersByCustomerId
- [ ] Implementar soft delete para órdenes
- [ ] Agregar historial de precios
- [ ] Notificaciones de cambio de estado
- [ ] Búsqueda de órdenes por rango de fechas
- [ ] Reportes de ventas

## Notas

- La BD se inicializa automáticamente en el primer arranque con schema.sql y data.sql
- Los números de orden se generan automáticamente si no se proporcionan
- Los totales se calculan automáticamente cuando se agrega/modifica una línea
- Todos los logs de estado se registran automáticamente


