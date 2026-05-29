package com.mircroservice.unir.orders.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Orden con id " + id + " no encontrada");
    }

    public OrderNotFoundException(String orderNumber) {
        super("Orden con número " + orderNumber + " no encontrada");
    }
}

