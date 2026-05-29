## Ejemplos de Uso de la API OrdersMicroservice

### Base URL
```
http://localhost:8082/api/orders
```

### 1. CREAR UNA NUEVA ORDEN

```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customer_id": 1,
    "billing_name": "Carlos López García",
    "billing_nif": "12345678B",
    "billing_address": "Avenida Central 999",
    "billing_city": "Barcelona",
    "billing_zip": "08002",
    "billing_country": "España",
    "tax_rate": 0.21,
    "notes": "Enviar con cuidado"
  }'
```

**Respuesta esperada:**
```json
{
  "id": 5,
  "order_number": "ORD-1716982020000-A1B2C3D4",
  "customer_id": 1,
  "status": "PENDING",
  "created_at": "2024-05-29T14:30:20",
  "updated_at": "2024-05-29T14:30:20",
  "billing_name": "Carlos López García",
  ...
}
```

---

### 2. AGREGAR UNA LÍNEA (LIBRO) A LA ORDEN

```bash
curl -X POST http://localhost:8082/api/orders/5/lines \
  -H "Content-Type: application/json" \
  -d '{
    "book_id": 1,
    "quantity": 2,
    "unit_price": 24.99,
    "discount_pct": 0
  }'
```

**Parámetros:**
- `book_id`: ID del libro del catálogo
- `quantity`: Cantidad (mínimo 1)
- `unit_price`: Precio unitario
- `discount_pct`: Porcentaje de descuento (0-100)

---

### 3. OBTENER UNA ORDEN POR ID

```bash
curl -X GET http://localhost:8082/api/orders/1
```

**Respuesta:**
```json
{
  "id": 1,
  "order_number": "ORD-2024-001",
  "customer_id": 1,
  "status": "PENDING",
  "created_at": "2024-05-29T10:30:00",
  "subtotal": 49.98,
  "tax_amount": 10.50,
  "total": 60.48,
  "lines": [
    {
      "id": 1,
      "book_id": 1,
      "quantity": 2,
      "unit_price": 24.99,
      "line_total": 49.98
    }
  ],
  "status_log": [
    {
      "id": 1,
      "from_status": "NONE",
      "to_status": "PENDING",
      "changed_at": "2024-05-29T10:30:00",
      "changed_by": "SYSTEM"
    }
  ]
}
```

---

### 4. OBTENER ORDEN POR NÚMERO DE ORDEN

```bash
curl -X GET http://localhost:8082/api/orders/number/ORD-2024-001
```

---

### 5. OBTENER TODAS LAS ÓRDENES DE UN CLIENTE

```bash
curl -X GET http://localhost:8082/api/orders/customer/1
```

**Respuesta:** Array de órdenes del cliente, ordenadas por fecha descendente

---

### 6. ACTUALIZAR CANTIDAD EN UNA LÍNEA

```bash
curl -X PATCH "http://localhost:8082/api/orders/1/lines/1/quantity?quantity=5"
```

---

### 7. ACTUALIZAR DESCUENTO EN UNA LÍNEA

```bash
curl -X PATCH "http://localhost:8082/api/orders/1/lines/1/discount?discountPct=10"
```

---

### 8. ACTUALIZAR INFORMACIÓN DE FACTURACIÓN

```bash
curl -X PUT http://localhost:8082/api/orders/1/billing \
  -H "Content-Type: application/json" \
  -d '{
    "billing_city": "Valencia",
    "billing_zip": "46001"
  }'
```

---

### 9. CONFIRMAR UNA ORDEN

Cambia estado de PENDING a CONFIRMED

```bash
curl -X POST http://localhost:8082/api/orders/1/confirm
```

---

### 10. MARCAR COMO ENVIADA

Cambia estado de CONFIRMED a SHIPPED

```bash
curl -X POST http://localhost:8082/api/orders/1/ship
```

---

### 11. MARCAR COMO ENTREGADA

Cambia estado de SHIPPED a DELIVERED

```bash
curl -X POST http://localhost:8082/api/orders/1/deliver
```

---

### 12. ELIMINAR UNA LÍNEA DE LA ORDEN

```bash
curl -X DELETE http://localhost:8082/api/orders/1/lines/1
```

---

### 13. CANCELAR UNA ORDEN

Cambia estado a CANCELLED (solo si está en PENDING o CONFIRMED)

```bash
curl -X DELETE http://localhost:8082/api/orders/1/cancel
```

---

### 14. ELIMINAR COMPLETAMENTE UNA ORDEN

Solo se puede si está en PENDING o CANCELLED

```bash
curl -X DELETE http://localhost:8082/api/orders/1
```

---

## FLUJO COMPLETO DE EJEMPLO

### Paso 1: Crear orden
```bash
ORDER_ID=$(curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customer_id": 2,
    "billing_name": "María García",
    "billing_nif": "87654321B",
    "billing_address": "Calle Mayor 456",
    "billing_city": "Madrid",
    "billing_zip": "28001",
    "billing_country": "España",
    "tax_rate": 0.21
  }' | grep '"id"' | head -1 | cut -d: -f2 | cut -d, -f1)

echo "Orden creada con ID: $ORDER_ID"
```

### Paso 2: Agregar primera línea
```bash
curl -X POST http://localhost:8082/api/orders/$ORDER_ID/lines \
  -H "Content-Type: application/json" \
  -d '{
    "book_id": 1,
    "quantity": 1,
    "unit_price": 24.99
  }'
```

### Paso 3: Agregar segunda línea
```bash
curl -X POST http://localhost:8082/api/orders/$ORDER_ID/lines \
  -H "Content-Type: application/json" \
  -d '{
    "book_id": 2,
    "quantity": 2,
    "unit_price": 29.99,
    "discount_pct": 5
  }'
```

### Paso 4: Ver la orden completa
```bash
curl -X GET http://localhost:8082/api/orders/$ORDER_ID
```

### Paso 5: Confirmar la orden
```bash
curl -X POST http://localhost:8082/api/orders/$ORDER_ID/confirm
```

### Paso 6: Actualizar descuento de una línea
```bash
curl -X PATCH "http://localhost:8082/api/orders/$ORDER_ID/lines/1/discount?discountPct=10"
```

### Paso 7: Ver historial de estados
```bash
curl -X GET http://localhost:8082/api/orders/$ORDER_ID | grep -A 20 "status_log"
```

### Paso 8: Enviar la orden
```bash
curl -X POST http://localhost:8082/api/orders/$ORDER_ID/ship
```

### Paso 9: Entregar la orden
```bash
curl -X POST http://localhost:8082/api/orders/$ORDER_ID/deliver
```

---

## CÓDIGOS DE ERROR ESPERADOS

### 400 - Bad Request
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "La cantidad debe ser mayor a 0"
}
```

### 404 - Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Orden con id 999 no encontrada"
}
```

### 409 - Conflict (Libro no disponible)
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Libro con id 1 no está disponible: El libro no es visible"
}
```

---

## VALIDACIONES IMPLEMENTADAS

✅ Número de orden único
✅ Cliente requerido
✅ Billig name requerido  
✅ Cantidad mínima 1
✅ Descuento entre 0-100%
✅ Estado solo transiciones válidas
✅ Validación de libro en catálogo (visible)
✅ No poder cancelar DELIVERED
✅ No poder eliminar CONFIRMED/SHIPPED/DELIVERED

---

## NOTAS

- Los números de orden se generan automáticamente si no se proporcionan
- Los totales se recalculan automáticamente después de cada cambio
- Todos los cambios de estado quedan registrados en el historial
- Las llamadas entre microservicios usan nombres de servicio (Eureka), no IPs


