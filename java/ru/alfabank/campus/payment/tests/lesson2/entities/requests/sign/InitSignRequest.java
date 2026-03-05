package ru.alfabank.campus.payment.tests.lesson2.entities.requests.sign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitSignRequest {

    @JsonProperty("xPin")
    private String userId;
    @JsonProperty("uPin")
    private String organizationId;
    private String paymentReference;

    public static InitSignRequest of(String userId, String organizationId, String paymentReference) {
        return InitSignRequest.builder()
                .userId(userId)
                .organizationId(organizationId)
                .paymentReference(paymentReference)
                .build();
    }
}
