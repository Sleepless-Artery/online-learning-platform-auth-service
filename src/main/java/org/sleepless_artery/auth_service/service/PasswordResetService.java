package org.sleepless_artery.auth_service.service;

import org.sleepless_artery.auth_service.dto.PasswordResetDto;


public interface PasswordResetService {

    void initiatePasswordReset(String emailAddress);

    boolean validatedResetCode(String emailAddress, String resetCode);

    void completePasswordReset(PasswordResetDto passwordResetDto);
}
