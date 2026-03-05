package ru.alfabank.campus.payment.tests.lesson8.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportData {

    private String passportSeries;
    private String passportNumber;

}
