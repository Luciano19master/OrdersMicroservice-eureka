## 🚀 GUÍA DE INICIO RÁPIDO

Para tener la aplicación completa funcionando en local, sigue estos pasos:

---

## Paso 1: Iniciar Eureka Server (Descubrimiento de Servicios)

```bash
cd "C:\Master ingeniería en software\Desarrollo Full Stack\Practica N2\back-end-eureka-master"
mvn spring-boot:run
```

**Esperado**: Puerto 8761 accesible
- URL: http://localhost:8761/eureka
- Verás una interfaz web con los servicios registrados

**Tiempo**: ~15 segundos

---

## Paso 2: Iniciar Catálogo Microservice

```bash
cd "C:\Master ingeniería en software\Desarrollo Full Stack\Practica N2\CatalogMicroservice"
mvn spring-boot:run
```

**Esperado**: Puerto 8081 accesible
- Endpoints: http://localhost:8081/api/books
- Se registra automáticamente en Eureka

**Tiempo**: ~10 segundos

---

## Paso 3: Iniciar OrdersMicroservice (NUEVO)

```bash
cd "C:\Master ingeniería en software\Desarrollo Full Stack\Practica N2\OrdersMicroservice-eureka"
mvn spring-boot:run
```

**Esperado**: Puerto 8082 accesible
- Endpoints: http://localhost:8082/api/orders
- Se registra automáticamente en Eureka
- Base de datos creada en: src/main/resources/database/orders.db

**Tiempo**: ~10 segundos

---

## Paso 4: Iniciar Cloud Gateway

```bash
cd "C:\Master ingeniería en software\Desarrollo Full Stack\Practica N2\back-end-cloud-gateway-master"
mvn spring-boot:run
```

**Esperado**: Puerto 8080 accesible
- URL: http://localhost:8080

**Tiempo**: ~10 segundos

---

## ✅ Verificación: Acceso a Servicios

Una vez que todos estén arriba, puedes acceder:

### Desde Gateway (Recomendado)
```bash
# Obtener orden
curl http://localhost:8080/orders/api/orders/1

# Ver libros
curl http://localhost:8080/catalogue/api/books
```

### Directamente (si usas Eureka)
```bash
# Órdenes
curl http://localhost:8082/api/orders/1

# Libros
curl http://localhost:8081/api/books

# Eureka
curl http://localhost:8761/eureka
```

---

## 📊 Dashboard Eureka

Abre en tu navegador:
```
http://localhost:8761/eureka
```

Deberías ver:
```
Instances currently registered with Eureka

CATALOGUE (1 instance)
ORDERSMICROSERVICE (1 instance)
```

---

## 🧪 Test Rápido: Crear una Orden

```bash
# 1. Crear orden
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customer_id": 1,
    "billing_name": "Test User",
    "billing_nif": "12345678A",
    "billing_address": "Test St 123",
    "billing_city": "Madrid",
    "billing_zip": "28001",
    "billing_country": "España",
    "tax_rate": 0.21
  }'

# 2. Agregar libro (reemplaza ORDER_ID con el ID retornado)
curl -X POST http://localhost:8082/api/orders/ORDER_ID/lines \
  -H "Content-Type: application/json" \
  -d '{
    "book_id": 1,
    "quantity": 1,
    "unit_price": 24.99
  }'

# 3. Ver orden creada
curl http://localhost:8082/api/orders/ORDER_ID
```

---

## ⚙️ Parar los Servicios

Presiona `Ctrl+C` en cada terminal para detener cada servicio.

---

## 🔧 Solución de Problemas

### Puerto ya en uso
```
Error: Address already in use
Solución: Cambia el puerto en application.yaml o termina el proceso
```

### Eureka no encuentra servicios
```
Espera 30 segundos después de iniciar
Los servicios tardan en registrarse
```

### Base de datos corrupta
```
Elimina: src/main/resources/database/orders.db
En el siguiente inicio se recrea automáticamente
```

### Error de compilación
```bash
mvn clean
mvn install -DskipTests
```

---

## 📝 Logs Importantes

### Eureka iniciado
```
Started EurekaApplication in X seconds
Tomcat started on port(s): 8761
```

### Catálogo iniciado
```
Started CatalogueApplication in X seconds
Tomcat started on port(s): 8081
DiscoveryClient initialized at timestamp
```

### OrdersMicroservice iniciado
```
Started OrdersMicroserviceApplication in X seconds
Tomcat started on port(s): 8082
DiscoveryClient initialized at timestamp
Database initialized successfully (si es primer arranque)
```

### Gateway iniciado
```
Started GatewayApplication in X seconds
Tomcat started on port(s): 8080
```

---

## 🎯 Próximas Pruebas

Una vez todo esté corriendo:

1. **Crear una orden**: Ver PROYECTO_COMPLETADO.md → Flujo Completo de Ejemplo
2. **Probar validación**: Intentar agregar un libro inexistente
3. **Ver historial**: Consultar order_status_log
4. **Simular compra completa**: PENDING → CONFIRMED → SHIPPED → DELIVERED

---

## 📚 Documentación Completa

Para más detalles, ver:
- **README.md** - Estructura y endpoints
- **API_EXAMPLES.md** - 100+ ejemplos de curl
- **PROYECTO_COMPLETADO.md** - Descripción técnica
- **VERIFICACION_FINAL.md** - Checklist de implementación

---

**¡Listo! Tu aplicación de e-commerce con microservicios está funcionando.** 🎉


