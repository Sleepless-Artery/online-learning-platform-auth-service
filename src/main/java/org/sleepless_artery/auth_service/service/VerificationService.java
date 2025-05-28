package org.sleepless_artery.auth_service.service;


public interface VerificationService {

    String saveVerificationCode(String emailAddress);

    boolean verifyAndDeleteCode(String emailAddress, String code);
}
