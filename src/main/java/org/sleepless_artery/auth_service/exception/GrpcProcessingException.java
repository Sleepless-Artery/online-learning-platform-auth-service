package org.sleepless_artery.auth_service.exception;

public class GrpcProcessingException extends RuntimeException {
    public GrpcProcessingException(String message) {
        super(message);
    }
}
