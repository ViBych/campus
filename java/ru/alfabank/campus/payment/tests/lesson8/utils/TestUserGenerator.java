package ru.alfabank.campus.payment.tests.lesson8.utils;

import ru.alfabank.campus.payment.tests.lesson2.gateways.AccountsApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.RolesApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.TestUserDataApiGateway;
import ru.alfabank.campus.payment.tests.lesson8.entities.user.FullUserResponce;
import ru.alfabank.campus.payment.tests.lesson8.entities.organizations.FullOrganizationsResponse;

public class TestUserGenerator {

    private final TestUserDataApiGateway userDataApiGateway = new TestUserDataApiGateway();
    private final AccountsApiGateway accountsApiGateway = new AccountsApiGateway();
    private final RolesApiGateway rolesApiGateway = new RolesApiGateway();

    private TestUser testUser;

    public TestUserGenerator generate() {
        FullUserResponce userResponce = userDataApiGateway.getUser();
        testUser = TestUser.builder()
            .userId(userResponce.getId())
            .firstName(userResponce.getFirstName())
            .lastName(userResponce.getLastName())
            .passportSeries(userResponce.getPassportData().getPassportSeries())
            .passportNumber(userResponce.getPassportData().getPassportNumber())
            .build();

        FullOrganizationsResponse senderOrg = userDataApiGateway.getOrganization();
        testUser.setSenderOrgId(senderOrg.getOrganizationId());
        testUser.setSenderOrgId(senderOrg.getOrganizationName().getShortName());

        FullOrganizationsResponse recipientOrg = userDataApiGateway.getOrganization();
        testUser.setSenderOrgId(recipientOrg.getOrganizationId());
        testUser.setSenderOrgId(recipientOrg.getOrganizationName().getShortName());

        return this;
    }

    public TestUser build() {
        return testUser;
    }
}
