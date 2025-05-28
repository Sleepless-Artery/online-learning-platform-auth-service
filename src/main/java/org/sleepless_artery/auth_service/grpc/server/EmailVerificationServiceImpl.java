package org.sleepless_artery.auth_service.grpc.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.sleepless_artery.auth_service.EmailAddressAvailabilityRequest;
import org.sleepless_artery.auth_service.EmailAddressAvailabilityResponse;
import org.sleepless_artery.auth_service.EmailVerificationServiceGrpc;
import org.sleepless_artery.auth_service.service.EmailReservationService;


@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EmailVerificationServiceImpl extends EmailVerificationServiceGrpc.EmailVerificationServiceImplBase {

    private final EmailReservationService emailReservationService;


    @Override
    public void isEmailAddressAvailable(
            EmailAddressAvailabilityRequest request, StreamObserver<EmailAddressAvailabilityResponse> streamObserver
    ) {
        boolean isAvailable = emailReservationService.isEmailAddressAvailable(request.getNewEmailAddress());

        try {
            streamObserver.onNext(EmailAddressAvailabilityResponse.newBuilder()
                    .setAvailability(isAvailable)
                    .setMessage(isAvailable ? "Email available" : "Email already in use")
                    .build());
            streamObserver.onCompleted();
        } catch (Exception e) {
            streamObserver.onError(Status.INTERNAL
                    .withDescription("Error checking email availability")
                    .asRuntimeException());
        }
    }
}
