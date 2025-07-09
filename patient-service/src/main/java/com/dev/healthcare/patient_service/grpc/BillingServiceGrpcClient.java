package com.dev.healthcare.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    public BillingServiceGrpcClient(
        @Value("${billing.service.address:localhost}") String serverAddress,
        @Value("${billing.service.grpc.port:9000}") int serverPort) {

        log.info("Connecting to Billing Service GRPC service at {}: {}", serverAddress, serverPort);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
            .usePlaintext()
            .build();

        billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    public BillingResponse createBillingAccount(String patientId, String email, String name) {

        BillingRequest billingRequest = BillingRequest.newBuilder()
            .setPatientId(patientId)
            .setEmail(email)
            .setName(name)
            .build();

        BillingResponse billingResponse = billingServiceBlockingStub.createBillingAccount(billingRequest);
        log.info("Received response from billing service via GRPC: {}", billingResponse.toString());

        return billingResponse;
    }

}
