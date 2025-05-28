package org.sleepless_artery.auth_service.service;

import java.time.Duration;


public interface EmailReservationService {

    boolean isEmailAddressAvailable(String emailAddress);

    void reserveEmailAddress(String emailAddress, Object reservationData, Duration reservationDuration);

    void checkReservation(String emailAddress, String confirmationCode);
}
