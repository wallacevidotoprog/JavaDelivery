package com.nanotech.javadelivery.delivery.tracking.domain.excepition;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException() {
    }
}
