package com.mircroservice.unir.orders.service;

import com.mircroservice.unir.orders.controller.model.OrderDto;
import com.mircroservice.unir.orders.dto.OrderMapper;
import com.mircroservice.unir.orders.exception.OrderNotFoundException;
import com.mircroservice.unir.orders.repository.OrderRepository;
import com.mircroservice.unir.orders.repository.model.Order;
import com.mircroservice.unir.orders.repository.model.OrderLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ModifyOrdersService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDto updateOrderLineQuantity(Long orderId, Long lineId, Integer newQuantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (newQuantity == null || newQuantity < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        OrderLine line = order.getLines().stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Línea no encontrada en la orden"));

        line.setQuantity(newQuantity);
        line.calculateLineTotal();

        // Recalcular totales de la orden
        recalculateOrderTotals(order);

        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrderLineDiscount(Long orderId, Long lineId, BigDecimal discountPct) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (discountPct == null || discountPct.compareTo(BigDecimal.ZERO) < 0 || discountPct.compareTo(new BigDecimal(100)) > 0) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }

        OrderLine line = order.getLines().stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Línea no encontrada en la orden"));

        line.setDiscountPct(discountPct);
        line.calculateLineTotal();

        // Recalcular totales de la orden
        recalculateOrderTotals(order);

        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrderBillingInfo(Long orderId, OrderDto billingDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (billingDto.billingName != null) {
            order.setBillingName(billingDto.billingName);
        }
        if (billingDto.billingNif != null) {
            order.setBillingNif(billingDto.billingNif);
        }
        if (billingDto.billingAddress != null) {
            order.setBillingAddress(billingDto.billingAddress);
        }
        if (billingDto.billingCity != null) {
            order.setBillingCity(billingDto.billingCity);
        }
        if (billingDto.billingZip != null) {
            order.setBillingZip(billingDto.billingZip);
        }
        if (billingDto.billingCountry != null) {
            order.setBillingCountry(billingDto.billingCountry);
        }

        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

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
}
