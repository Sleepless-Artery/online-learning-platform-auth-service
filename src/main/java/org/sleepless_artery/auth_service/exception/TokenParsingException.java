package org.sleepless_artery.auth_service.exception;

public class TokenParsingException extends RuntimeException {
    public TokenParsingException(String message) {
        super(message);
    }
}
