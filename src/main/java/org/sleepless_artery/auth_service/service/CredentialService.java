package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.dto.RegistrationCredentialDto;
import org.sleepless_artery.auth_service.model.Credential;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface CredentialService extends UserDetailsService {

    Credential createCredential(RegistrationCredentialDto registrationCredentialDto);

    Credential findCredentialByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);

    Credential save(Credential credential);

    void changeEmailAddress(String oldEmailAddress, String newEmailAddress);

    void deleteByEmailAddress(String emailAddress);
}