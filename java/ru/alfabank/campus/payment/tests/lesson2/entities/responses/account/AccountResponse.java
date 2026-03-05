package ru.alfabank.campus.payment.tests.lesson2.entities.responses.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

    private String accountNumberFull;
    private Double balance;
}
