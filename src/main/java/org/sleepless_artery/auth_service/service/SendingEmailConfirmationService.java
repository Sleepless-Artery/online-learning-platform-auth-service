package org.sleepless_artery.auth_service.service;

public interface SendingEmailConfirmationService {

    void sendEmailConfirmation(String emailAddress, String postfix);
}
