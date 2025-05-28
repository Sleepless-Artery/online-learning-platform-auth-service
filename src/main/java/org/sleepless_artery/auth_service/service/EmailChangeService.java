package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.dto.CredentialDto;


public interface EmailChangeService {

    void changeEmailAddress(CredentialDto credentialDto, String newEmailAddress);

    void confirmEmailAddress(String oldEmailAddress, String newEmailAddress, String confirmationCode);
}
