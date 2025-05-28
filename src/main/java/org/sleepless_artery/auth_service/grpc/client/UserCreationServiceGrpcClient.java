package org.sleepless_artery.auth_service.grpc.client;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sleepless_artery.auth_service.CreateUserRequest;
import org.sleepless_artery.auth_service.config.grpc.GrpcClientConfig;
import org.sleepless_artery.auth_service.exception.GrpcProcessingException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreationServiceGrpcClient {

    private final GrpcClientConfig grpcClientConfig;


    public boolean createUser(String emailAddress) {
        log.info("Sending gRPC request to user-service for creating user with email address '{}'", emailAddress);

        try {
            CreateUserRequest request = CreateUserRequest.newBuilder()
                    .setEmailAddress(emailAddress)
                    .build();

            return grpcClientConfig.uerCreationServiceBlockingStub()
                    .withDeadlineAfter(30, TimeUnit.SECONDS)
                    .createUser(request)
                    .getSuccess();
        } catch (StatusRuntimeException e) {
            log.error("gRPC error: {}", e.getStatus());
            throw new GrpcProcessingException("Failed to create user");
        }
    }
}
