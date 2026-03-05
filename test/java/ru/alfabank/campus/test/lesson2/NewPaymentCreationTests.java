package ru.alfabank.campus.test.lesson2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson2.gateways.AccountsApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.PaymentApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.PaymentProxyApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.RolesApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.SignApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.TestUserDataApiGateway;

import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.CREATE_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.SIGN_PAYMENT;

public class NewPaymentCreationTests {

    private final TestUserDataApiGateway userDataApiGateway = new TestUserDataApiGateway();
    private final AccountsApiGateway accountsApiGateway = new AccountsApiGateway();
    private final RolesApiGateway rolesApiGateway = new RolesApiGateway();
    private final PaymentApiGateway paymentApiGateway = new PaymentApiGateway();
    private final PaymentProxyApiGateway paymentProxyApiGateway = new PaymentProxyApiGateway();
    private final SignApiGateway signApiGateway = new SignApiGateway();

    @Test
    @DisplayName("Проверка создания платежа")
    void shouldCreatePayment() {
        //Создание пользователя и организаций
        String userId = userDataApiGateway.getUserId();
        String senderOrganizationId = userDataApiGateway.getOrganizationId();
        String recipientOrganizationId = userDataApiGateway.getOrganizationId();

        //Создание счетов
        String accountNumber = accountsApiGateway.createAccount(senderOrganizationId);

        //Добавление прав
        rolesApiGateway.addRight(userId, senderOrganizationId, CREATE_PAYMENT);
        rolesApiGateway.addRight(userId, senderOrganizationId, SIGN_PAYMENT);

        //Создание платежа
//        String paymentRef = paymentApiGateway.getPaymentRef(userId, senderOrganizationId, recipientOrganizationId,
//                accountNumber, 4200.00);
//        System.out.println(paymentProxyApiGateway.getPaymentOperationStatus(paymentRef));
//
//        //Инициализация подписи
//        String singRef = signApiGateway.initSign(userId, senderOrganizationId, paymentRef);
//        System.out.println(paymentProxyApiGateway.getPaymentOperationStatus(paymentRef));
//
//        //Подтверждение подписи
//        signApiGateway.confirmSign(userId, senderOrganizationId, singRef);
//        System.out.println(paymentProxyApiGateway.getPaymentOperationStatus(paymentRef));


    }

}
