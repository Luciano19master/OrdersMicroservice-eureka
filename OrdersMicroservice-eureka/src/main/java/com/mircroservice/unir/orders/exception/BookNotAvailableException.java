package com.mircroservice.unir.orders.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(Long bookId) {
        super("Libro con id " + bookId + " no está disponible o no existe");
    }

    public BookNotAvailableException(Long bookId, String reason) {
        super("Libro con id " + bookId + " no está disponible: " + reason);
    }
}

