package ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentProxyResponse {

    private String status;
}
