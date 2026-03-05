package ru.alfabank.campus.payment.tests.lesson2.entities.responses.orginazation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationResponse {

    private String organizationId;
}
