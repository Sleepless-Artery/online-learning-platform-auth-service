package org.sleepless_artery.auth_service.config.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.sleepless_artery.auth_service.UserCreationServiceGrpc;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcClientConfig {

    @GrpcClient("user-service")
    private UserCreationServiceGrpc.UserCreationServiceBlockingStub stub;

    public UserCreationServiceGrpc.UserCreationServiceBlockingStub uerCreationServiceBlockingStub() {
        return stub;
    }
}
