package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.dto.JwtResponse;
import org.sleepless_artery.auth_service.dto.RegistrationCredentialDto;


public interface RegistrationService {

    void startRegistration(RegistrationCredentialDto registrationCredentialDto);

    JwtResponse confirmRegistration(RegistrationCredentialDto registrationCredentialDto, String confirmationCode);
}
