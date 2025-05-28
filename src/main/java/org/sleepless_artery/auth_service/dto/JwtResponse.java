package org.sleepless_artery.auth_service.dto;

import java.io.Serializable;


public record JwtResponse(String token) implements Serializable {
    private static final long serialVersionUID = 1L;
}
