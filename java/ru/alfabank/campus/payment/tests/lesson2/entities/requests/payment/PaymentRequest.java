package ru.alfabank.campus.payment.tests.lesson2.entities.requests.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    private Sender from;
    private Recipient to;
    private double amount;

    public static PaymentRequest of(String senderUserId, String senderOrganizationId,
                                    String recipientOrganizationId, String accountNumber,
                                    double amount) {
        return PaymentRequest.builder()
                .from(Sender.builder()
                        .userId(senderUserId)
                        .organizationId(senderOrganizationId)
                        .accountNumber(accountNumber)
                        .build())
                .to(Recipient.builder()
                        .organizationId(recipientOrganizationId)
                        .build())
                .amount(amount)
                .build();
    }

    @Data
    @Builder
    public static class Sender {
        private String userId;
        private String organizationId;
        private String accountNumber;
    }

    @Data
    @Builder
    public static class Recipient {
        private String organizationId;
    }
}
