package org.sleepless_artery.auth_service.exception;

public class CredentialAlreadyExistsException extends RuntimeException {
    public CredentialAlreadyExistsException(String message) {
        super(message);
    }
}
