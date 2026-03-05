package ru.alfabank.campus.payment.tests.lesson2.entities.responses.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignResponse {

    private String signReference;
}
