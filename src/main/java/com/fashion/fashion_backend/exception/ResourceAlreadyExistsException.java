package com.fashion.fashion_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // HTTP 409
public class ResourceAlreadyExistsException extends RuntimeException {

    // UYARIYI GİDEREN SATIR
    private static final long serialVersionUID = 2L; // Genellikle farklı bir numara verilir.

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}