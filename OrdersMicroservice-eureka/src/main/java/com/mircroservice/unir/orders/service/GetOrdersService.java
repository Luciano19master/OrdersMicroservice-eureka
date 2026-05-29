package com.mircroservice.unir.orders.service;

import com.mircroservice.unir.orders.controller.model.OrderDto;
import com.mircroservice.unir.orders.dto.OrderMapper;
import com.mircroservice.unir.orders.exception.OrderNotFoundException;
import com.mircroservice.unir.orders.repository.OrderRepository;
import com.mircroservice.unir.orders.repository.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrdersService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toDto(order);
    }

    public OrderDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        return orderMapper.toDto(order);
    }

    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<OrderDto> getAllOrdersPages(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }
}

