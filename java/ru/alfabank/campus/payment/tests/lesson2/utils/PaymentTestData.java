package ru.alfabank.campus.payment.tests.lesson2.utils;

import lombok.Data;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.payment.PaymentRequest;
import ru.alfabank.campus.payment.tests.lesson2.enums.UserRights;
import ru.alfabank.campus.payment.tests.lesson2.gateways.AccountsApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.RolesApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.TestUserDataApiGateway;

import java.util.List;

@Data
public class PaymentTestData {

    private static final TestUserDataApiGateway USER_DATA_API_GATEWAY = new TestUserDataApiGateway();
    private static final AccountsApiGateway ACCOUNTS_API_GATEWAY = new AccountsApiGateway();
    private static final RolesApiGateway ROLES_API_GATEWAY = new RolesApiGateway();

    private String userId;
    private String senderOrganizationId;
    private String recipientOrganizationId;
    private String accountNumber;
    private Double balance;

    public static PaymentTestData generate() {
        PaymentTestData data = new PaymentTestData();
        data.userId = USER_DATA_API_GATEWAY.getUserId();
        data.senderOrganizationId = USER_DATA_API_GATEWAY.getOrganizationId();
        data.recipientOrganizationId = USER_DATA_API_GATEWAY.getOrganizationId();
        data.accountNumber = ACCOUNTS_API_GATEWAY.createAccount(data.senderOrganizationId);
        return data;
    }

    public static PaymentTestData generate(List<UserRights> rights) {
        PaymentTestData data = generate();
        data.addRights(rights);
        return data;
    }

    public PaymentTestData addRights(List<UserRights> rights) {
        for (UserRights right : rights) {
            ROLES_API_GATEWAY.addRight(userId, senderOrganizationId, right);
        }
        return this;
    }

    public PaymentRequest toPaymentRequest(double amount) {
        return PaymentRequest.of(userId, senderOrganizationId, recipientOrganizationId, accountNumber, amount);
    }

    public PaymentRequest toPaymentRequestWithCustomAccountNumber(String accountNumber, double amount) {
        return PaymentRequest.of(userId, senderOrganizationId, recipientOrganizationId, accountNumber, amount);
    }

    public PaymentRequest toPaymentRequestWithCustomRecipientOrganizationId(String recipientOrganizationId, double amount) {
        return PaymentRequest.of(userId, senderOrganizationId, recipientOrganizationId, accountNumber, amount);
    }

    public Object[] permissionDeniedArgs(String permission) {
        return new Object[] {userId, permission, senderOrganizationId};
    }

}
