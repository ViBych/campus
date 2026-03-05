package ru.alfabank.campus.payment.tests.lesson2.entities.requests.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountsRequest {

    @JsonProperty("uPin")
    private String organizationId;
    private String currency;
    private String accountInfo;

    public static AccountsRequest of(String organizationId) {
        return AccountsRequest.builder()
                .organizationId(organizationId)
                .currency("RUR")
                .accountInfo("PAYMENT_RUR")
                .build();
    }

}
