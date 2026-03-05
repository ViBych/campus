package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.requests.roles.RoleRequest;
import ru.alfabank.campus.payment.tests.lesson2.enums.UserRights;

public class RolesApiGateway extends BaseApiGateway {

    public RolesApiGateway() {
        super("/roles-api");
    }

    public void addRight(String userId, String organizationId, UserRights rightName) {
        RoleRequest body = RoleRequest.of(userId, organizationId, rightName);
        processRight(body);
    }

    public void addRight(RoleRequest body) {
        processRight(body);
    }

    private void processRight(RoleRequest body) {
        post(body, "/api/roles/add")
                .statusCode(200);
    }
}
