package com.mircroservice.unir.orders.service;

import com.mircroservice.unir.orders.dto.OrderMapper;
import com.mircroservice.unir.orders.exception.OrderNotFoundException;
import com.mircroservice.unir.orders.repository.OrderRepository;
import com.mircroservice.unir.orders.repository.OrderStatusLogRepository;
import com.mircroservice.unir.orders.repository.model.Order;
import com.mircroservice.unir.orders.repository.model.OrderStatusLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeleteOrdersService {

    private final OrderRepository orderRepository;
    private final OrderStatusLogRepository orderStatusLogRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if ("CANCELLED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new IllegalArgumentException("No se puede cancelar una orden en estado " + order.getStatus());
        }

        String previousStatus = order.getStatus();
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // Agregar log de estado
        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setFromStatus(previousStatus);
        log.setToStatus("CANCELLED");
        log.setChangedAt(LocalDateTime.now());
        log.setChangedBy("SYSTEM");
        orderStatusLogRepository.save(log);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if ("CONFIRMED".equals(order.getStatus()) || "SHIPPED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new IllegalArgumentException("No se puede eliminar una orden en estado " + order.getStatus() + ". Solo se pueden eliminar órdenes en estado PENDING o CANCELLED");
        }

        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void deleteOrderLine(Long orderId, Long lineId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.getLines().removeIf(l -> l.getId().equals(lineId));

        orderRepository.save(order);
    }

    @Transactional
    public void markAsShipped(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!"CONFIRMED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Solo se pueden enviar órdenes en estado CONFIRMED");
        }

        String previousStatus = order.getStatus();
        order.setStatus("SHIPPED");
        order.setShippedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // Agregar log de estado
        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setFromStatus(previousStatus);
        log.setToStatus("SHIPPED");
        log.setChangedAt(LocalDateTime.now());
        log.setChangedBy("SYSTEM");
        orderStatusLogRepository.save(log);
    }

    @Transactional
    public void markAsDelivered(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!"SHIPPED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Solo se pueden marcar como entregadas órdenes en estado SHIPPED");
        }

        String previousStatus = order.getStatus();
        order.setStatus("DELIVERED");
        order.setDeliveredAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // Agregar log de estado
        OrderStatusLog log = new OrderStatusLog();
        log.setOrder(order);
        log.setFromStatus(previousStatus);
        log.setToStatus("DELIVERED");
        log.setChangedAt(LocalDateTime.now());
        log.setChangedBy("SYSTEM");
        orderStatusLogRepository.save(log);
    }
}
