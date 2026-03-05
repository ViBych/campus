package ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

    private String status;
    private String reference;
    private Double amount;
    private PaymentResponse.Sender from;
    private PaymentResponse.Recipient to;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sender {
        private String userId;
        private String organizationId;
        private String accountNumber;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipient {
        private String organizationId;
    }
}
