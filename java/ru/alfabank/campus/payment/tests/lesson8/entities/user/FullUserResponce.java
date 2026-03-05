package ru.alfabank.campus.payment.tests.lesson8.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullUserResponce {

    private String id;
    private String firstName;
    private String lastName;
    private PassportData passportData;
}
