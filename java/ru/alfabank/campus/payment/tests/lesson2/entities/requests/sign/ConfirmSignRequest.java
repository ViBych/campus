package ru.alfabank.campus.payment.tests.lesson2.entities.requests.sign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmSignRequest {

    @JsonProperty("xPin")
    private String userId;
    @JsonProperty("uPin")
    private String organizationId;
    private String signReference;

    public static ConfirmSignRequest of(String userId, String organizationId, String signReference) {
        return ConfirmSignRequest.builder()
                .userId(userId)
                .organizationId(organizationId)
                .signReference(signReference)
                .build();
    }
}
