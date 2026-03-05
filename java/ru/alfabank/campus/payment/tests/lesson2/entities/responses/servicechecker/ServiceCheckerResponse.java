package ru.alfabank.campus.payment.tests.lesson2.entities.responses.servicechecker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCheckerResponse {

    private String service;
    private String test;
}
