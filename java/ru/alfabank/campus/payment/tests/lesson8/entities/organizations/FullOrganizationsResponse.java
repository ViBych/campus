package ru.alfabank.campus.payment.tests.lesson8.entities.organizations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullOrganizationsResponse {

    private String organizationId;
    private OrganizationName organizationName;
    private SupportInfo supportInfo;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrganizationName {
        private String shortName;
        private String fullName;
        private String embName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SupportInfo {
        private String inn;
        private String kpp;
        private String ogrn;
    }
}
