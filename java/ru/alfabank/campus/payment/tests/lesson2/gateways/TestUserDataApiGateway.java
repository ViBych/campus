package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.responses.orginazation.OrganizationResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.user.UserResponse;
import ru.alfabank.campus.payment.tests.lesson8.entities.user.FullUserResponce;
import ru.alfabank.campus.payment.tests.lesson8.entities.organizations.FullOrganizationsResponse;

public class TestUserDataApiGateway extends BaseApiGateway {

    public TestUserDataApiGateway() {
        super("/test-user-data-api");
    }

    public String getUserId() {
        UserResponse user = post("/api/users/generate", UserResponse.class);
        return user.getId();
    }

    public String getOrganizationId() {
        OrganizationResponse organization = post("/api/organizations/generate", OrganizationResponse.class);
        return organization.getOrganizationId();
    }

    public FullUserResponce getUser() {
        return post("/api/user/generate", FullUserResponce.class);
    }

    public FullOrganizationsResponse getOrganization() {
        return post("/api/organizations/generate", FullOrganizationsResponse.class);
    }

}
