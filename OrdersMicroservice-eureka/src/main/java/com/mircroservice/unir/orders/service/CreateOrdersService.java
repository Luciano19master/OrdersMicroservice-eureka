package com.mircroservice.unir.orders.service;

import com.mircroservice.unir.orders.controller.model.OrderDto;
import com.mircroservice.unir.orders.controller.model.OrderLineDto;
import com.mircroservice.unir.orders.dto.OrderMapper;
import com.mircroservice.unir.orders.exception.BookNotAvailableException;
import com.mircroservice.unir.orders.exception.OrderNotFoundException;
import com.mircroservice.unir.orders.repository.OrderRepository;
import com.mircroservice.unir.orders.repository.OrderStatusLogRepository;
import com.mircroservice.unir.orders.repository.model.Order;
import com.mircroservice.unir.orders.repository.model.OrderLine;
import com.mircroservice.unir.orders.repository.model.OrderStatusLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrdersService {

    private final OrderRepository orderRepository;
    private final OrderStatusLogRepository orderStatusLogRepository;
    private final OrderMapper orderMapper;
    private final RestTemplate restTemplate;

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // Validar que no existe el orderNumber
        if (orderDto.orderNumber != null && orderRepository.existsByOrderNumber(orderDto.orderNumber)) {
            throw new IllegalArgumentException("Ya existe una orden con ese número: " + orderDto.orderNumber);
        }

        // Crear la orden
        Order order = new Order();
        if (orderDto.orderNumber == null) {
            order.setOrderNumber(generateOrderNumber());
        } else {
            order.setOrderNumber(orderDto.orderNumber);
        }
        order.setCustomerId(orderDto.customerId.longValue());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setBillingName(orderDto.billingName);
        order.setBillingNif(orderDto.billingNif);
        order.setBillingAddress(orderDto.billingAddress);
        order.setBillingCity(orderDto.billingCity);
        order.setBillingZip(orderDto.billingZip);
        order.setBillingCountry(orderDto.billingCountry);
        order.setSubtotal(BigDecimal.ZERO);
        order.setDiscount(orderDto.discount != null ? orderDto.discount : BigDecimal.ZERO);
        order.setTaxRate(orderDto.taxRate != null ? orderDto.taxRate : BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotal(BigDecimal.ZERO);
        order.setNotes(orderDto.notes);

        // Guardar la orden
        Order savedOrder = orderRepository.save(order);

        // Agregar log de estado
        addStatusLog(savedOrder, null, "PENDING", "SYSTEM");

        // Convertir y retornar
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto addOrderLine(Long orderId, OrderLineDto lineDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Validar que el libro existe y está disponible
        validateBookAvailability(lineDto.bookId.longValue());

        OrderLine line = new OrderLine();
        line.setBookId(lineDto.bookId.longValue());
        line.setQuantity(lineDto.quantity);
        line.setUnitPrice(lineDto.unitPrice);
        line.setDiscountPct(lineDto.discountPct != null ? lineDto.discountPct : BigDecimal.ZERO);
        line.calculateLineTotal();

        order.addOrderLine(line);

        // Recalcular totales
        recalculateOrderTotals(order);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto removeOrderLine(Long orderId, Long lineId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderLine lineToRemove = order.getLines().stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Línea no encontrada en la orden"));

        order.removeOrderLine(lineToRemove);

        // Recalcular totales
        recalculateOrderTotals(order);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getLines().isEmpty()) {
            throw new IllegalArgumentException("La orden debe tener al menos una línea");
        }

        String previousStatus = order.getStatus();
        order.setStatus("CONFIRMED");
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Agregar log de estado
        addStatusLog(savedOrder, previousStatus, "CONFIRMED", "SYSTEM");

        return orderMapper.toDto(savedOrder);
    }

    private void recalculateOrderTotals(Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderLine line : order.getLines()) {
            line.calculateLineTotal();
            subtotal = subtotal.add(line.getLineTotal());
        }

        order.setSubtotal(subtotal);

        BigDecimal discount = order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discount);

        BigDecimal taxRate = order.getTaxRate() != null ? order.getTaxRate() : BigDecimal.ZERO;
        BigDecimal taxAmount = taxableAmount.multiply(taxRate).divide(new BigDecimal(100));

        order.setTaxAmount(taxAmount);
        order.setTotal(taxableAmount.add(taxAmount));
    }

    private void validateBookAvailability(Long bookId) {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                    "http://catalogue/api/books/{id}/validate",
                    Map.class,
                    bookId
            );

            if (response == null) {
                throw new BookNotAvailableException(bookId, "El libro no es visible");
            }
        } catch (Exception e) {
            log.error("Error validando libro " + bookId, e);
            throw new BookNotAvailableException(bookId, "Error al validar la disponibilidad del libro");
        }
    }

    private void addStatusLog(Order order, String fromStatus, String toStatus, String changedBy) {
        OrderStatusLog log = new OrderStatusLog();
        log.setFromStatus(fromStatus != null ? fromStatus : "NONE");
        log.setToStatus(toStatus);
        log.setChangedAt(LocalDateTime.now());
        log.setChangedBy(changedBy);
        order.addStatusLog(log);
        orderStatusLogRepository.save(log);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
