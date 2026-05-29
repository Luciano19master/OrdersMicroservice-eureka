package com.mircroservice.unir.orders.dto;

import com.mircroservice.unir.orders.controller.model.OrderDto;
import com.mircroservice.unir.orders.controller.model.OrderLineDto;
import com.mircroservice.unir.orders.controller.model.OrderStatusLogDto;
import com.mircroservice.unir.orders.repository.model.Order;
import com.mircroservice.unir.orders.repository.model.OrderLine;
import com.mircroservice.unir.orders.repository.model.OrderStatusLog;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDto.builder()
                .id(order.getId().intValue())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId().intValue())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .billingName(order.getBillingName())
                .billingNif(order.getBillingNif())
                .billingAddress(order.getBillingAddress())
                .billingCity(order.getBillingCity())
                .billingZip(order.getBillingZip())
                .billingCountry(order.getBillingCountry())
                .subtotal(order.getSubtotal())
                .discount(order.getDiscount())
                .taxRate(order.getTaxRate())
                .taxAmount(order.getTaxAmount())
                .total(order.getTotal())
                .notes(order.getNotes())
                .lines(order.getLines().stream().map(this::lineToDto).collect(Collectors.toList()))
                .statusLog(order.getStatusLog().stream().map(this::statusLogToDto).collect(Collectors.toList()))
                .build();
    }

    public Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        if (dto.id != null) {
            order.setId(dto.id.longValue());
        }
        order.setOrderNumber(dto.orderNumber);
        if (dto.customerId != null) {
            order.setCustomerId(dto.customerId.longValue());
        }
        order.setStatus(dto.status);
        order.setCreatedAt(dto.createdAt);
        order.setUpdatedAt(dto.updatedAt);
        order.setShippedAt(dto.shippedAt);
        order.setDeliveredAt(dto.deliveredAt);
        order.setBillingName(dto.billingName);
        order.setBillingNif(dto.billingNif);
        order.setBillingAddress(dto.billingAddress);
        order.setBillingCity(dto.billingCity);
        order.setBillingZip(dto.billingZip);
        order.setBillingCountry(dto.billingCountry);
        order.setSubtotal(dto.subtotal);
        order.setDiscount(dto.discount);
        order.setTaxRate(dto.taxRate);
        order.setTaxAmount(dto.taxAmount);
        order.setTotal(dto.total);
        order.setNotes(dto.notes);
        if (dto.lines != null) {
            order.setLines(dto.lines.stream().map(this::lineDtoToEntity).collect(Collectors.toList()));
        }
        if (dto.statusLog != null) {
            order.setStatusLog(dto.statusLog.stream().map(this::statusLogDtoToEntity).collect(Collectors.toList()));
        }
        return order;
    }

    public OrderLineDto lineToDto(OrderLine line) {
        if (line == null) {
            return null;
        }
        return OrderLineDto.builder()
                .id(line.getId().intValue())
                .orderId(line.getOrder().getId().intValue())
                .bookId(line.getBookId().intValue())
                .quantity(line.getQuantity())
                .unitPrice(line.getUnitPrice())
                .discountPct(line.getDiscountPct())
                .lineTotal(line.getLineTotal())
                .build();
    }

    public OrderLine lineDtoToEntity(OrderLineDto dto) {
        if (dto == null) {
            return null;
        }
        OrderLine line = new OrderLine();
        if (dto.id != null) {
            line.setId(dto.id.longValue());
        }
        if (dto.bookId != null) {
            line.setBookId(dto.bookId.longValue());
        }
        line.setQuantity(dto.quantity);
        line.setUnitPrice(dto.unitPrice);
        line.setDiscountPct(dto.discountPct);
        line.setLineTotal(dto.lineTotal);
        return line;
    }

    public OrderStatusLogDto statusLogToDto(OrderStatusLog log) {
        if (log == null) {
            return null;
        }
        return OrderStatusLogDto.builder()
                .id(log.getId().intValue())
                .orderId(log.getOrder().getId().intValue())
                .fromStatus(log.getFromStatus())
                .toStatus(log.getToStatus())
                .changedAt(log.getChangedAt())
                .changedBy(log.getChangedBy())
                .build();
    }

    public OrderStatusLog statusLogDtoToEntity(OrderStatusLogDto dto) {
        if (dto == null) {
            return null;
        }
        OrderStatusLog log = new OrderStatusLog();
        if (dto.id != null) {
            log.setId(dto.id.longValue());
        }
        log.setFromStatus(dto.fromStatus);
        log.setToStatus(dto.toStatus);
        log.setChangedAt(dto.changedAt);
        log.setChangedBy(dto.changedBy);
        return log;
    }
}

