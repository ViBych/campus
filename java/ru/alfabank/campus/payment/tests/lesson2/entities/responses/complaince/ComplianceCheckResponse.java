package ru.alfabank.campus.payment.tests.lesson2.entities.responses.complaince;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplianceCheckResponse {

    private String result;
    private String paymentReference;
    private String paymentStatus;
}
