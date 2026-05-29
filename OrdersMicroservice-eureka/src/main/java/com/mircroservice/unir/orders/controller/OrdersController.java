package com.mircroservice.unir.orders.controller;

import com.mircroservice.unir.orders.controller.model.OrderDto;
import com.mircroservice.unir.orders.controller.model.OrderLineDto;
import com.mircroservice.unir.orders.service.CreateOrdersService;
import com.mircroservice.unir.orders.service.DeleteOrdersService;
import com.mircroservice.unir.orders.service.GetOrdersService;
import com.mircroservice.unir.orders.service.ModifyOrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final CreateOrdersService createOrdersService;
    private final GetOrdersService getOrdersService;
    private final ModifyOrdersService modifyOrdersService;
    private final DeleteOrdersService deleteOrdersService;

    // ========== GET Endpoints ==========

    /**
     * Obtiene todas las órdenes (sin paginación)
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(getOrdersService.getAllOrders());
    }

    /**
     * Obtiene todas las órdenes con paginación
     * ?page=0&size=20&sort=createdAt,desc
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<OrderDto>> getAllOrdersPaginated(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(getOrdersService.getAllOrdersPages(pageable));
    }

    /**
     * Obtiene una orden por ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(getOrdersService.getOrderById(orderId));
    }

    /**
     * Obtiene una orden por número de orden
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(getOrdersService.getOrderByOrderNumber(orderNumber));
    }

    /**
     * Obtiene todas las órdenes recientes de un cliente (para el perfil)
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(getOrdersService.getOrdersByCustomerId(customerId));
    }

    // ========== POST Endpoints ==========

    /**
     * Crea una nueva orden
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(orderDto));
    }

    /**
     * Agrega una línea (item) a una orden
     */
    @PostMapping("/{orderId}/lines")
    public ResponseEntity<OrderDto> addOrderLine(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderLineDto lineDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                createOrdersService.addOrderLine(orderId, lineDto)
        );
    }

    /**
     * Confirma una orden (cambia estado a CONFIRMED)
     */
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(createOrdersService.confirmOrder(orderId));
    }

    /**
     * Marca una orden como enviada (SHIPPED)
     */
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<OrderDto> markAsShipped(@PathVariable Long orderId) {
        deleteOrdersService.markAsShipped(orderId);
        return ResponseEntity.ok(getOrdersService.getOrderById(orderId));
    }

    /**
     * Marca una orden como entregada (DELIVERED)
     */
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<OrderDto> markAsDelivered(@PathVariable Long orderId) {
        deleteOrdersService.markAsDelivered(orderId);
        return ResponseEntity.ok(getOrdersService.getOrderById(orderId));
    }

    // ========== PUT Endpoints ==========

    /**
     * Actualiza la información de facturación de una orden
     */
    @PutMapping("/{orderId}/billing")
    public ResponseEntity<OrderDto> updateOrderBillingInfo(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderDto billingDto
    ) {
        return ResponseEntity.ok(modifyOrdersService.updateOrderBillingInfo(orderId, billingDto));
    }

    // ========== PATCH Endpoints ==========

    /**
     * Actualiza la cantidad de una línea de orden
     */
    @PatchMapping("/{orderId}/lines/{lineId}/quantity")
    public ResponseEntity<OrderDto> updateLineQuantity(
            @PathVariable Long orderId,
            @PathVariable Long lineId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(modifyOrdersService.updateOrderLineQuantity(orderId, lineId, quantity));
    }

    /**
     * Actualiza el descuento de una línea de orden
     */
    @PatchMapping("/{orderId}/lines/{lineId}/discount")
    public ResponseEntity<OrderDto> updateLineDiscount(
            @PathVariable Long orderId,
            @PathVariable Long lineId,
            @RequestParam java.math.BigDecimal discountPct
    ) {
        return ResponseEntity.ok(modifyOrdersService.updateOrderLineDiscount(orderId, lineId, discountPct));
    }

    // ========== DELETE Endpoints ==========

    /**
     * Elimina una línea de una orden
     */
    @DeleteMapping("/{orderId}/lines/{lineId}")
    public ResponseEntity<OrderDto> deleteOrderLine(
            @PathVariable Long orderId,
            @PathVariable Long lineId
    ) {
        return ResponseEntity.ok(createOrdersService.removeOrderLine(orderId, lineId));
    }

    /**
     * Cancela una orden
     */
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        deleteOrdersService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina completamente una orden (solo si no está confirmada/enviada/entregada)
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        deleteOrdersService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
