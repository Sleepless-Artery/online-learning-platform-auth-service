package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.dto.CredentialDto;
import org.sleepless_artery.auth_service.dto.JwtResponse;


public interface AuthService {

    JwtResponse createAuthenticationToken(CredentialDto credentialDto);

    void logout(String emailAddress);
}
