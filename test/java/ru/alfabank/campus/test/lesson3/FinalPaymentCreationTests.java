package ru.alfabank.campus.test.lesson3;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.payment.PaymentRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.sign.SignResponse;
import ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage;
import ru.alfabank.campus.payment.tests.lesson2.gateways.*;
import ru.alfabank.campus.payment.tests.lesson2.utils.OperationChecker;
import ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData;

import java.util.List;

import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentStatuses.*;
import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage.INSUFFICIENT_FUNDS;
import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage.PERMISSION_DENIED;
import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage.ACCOUNT_ACCESS_DENIED;
import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage.RECIPIENT_ORGANIZATION_NOT_FOUND;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.CREATE_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.SIGN_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData.*;

public class FinalPaymentCreationTests {

    private final PaymentApiGateway paymentApiGateway = new PaymentApiGateway();
    private final OperationChecker checker = new OperationChecker();
    private final SignApiGateway signApiGateway = new SignApiGateway();
    private final AccountsApiGateway accountsApiGateway = new AccountsApiGateway();
    private final TestUserDataApiGateway testUserDataApiGateway = new TestUserDataApiGateway();
    private final PaymentProxyApiGateway paymentProxyApiGateway = new PaymentProxyApiGateway();

    @Test
    @DisplayName("Успешная обработка платежа")
    void shouldCreatePayment() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));

        PaymentRequest request = testData.toPaymentRequest(4200.00);

        String paymentRef = paymentApiGateway.getPaymentRef(request);
        checker.verifyOperationStatus(paymentRef, NEW.getValue());

        String signRef = signApiGateway.initSign(testData, paymentRef);
        checker.verifyOperationStatus(paymentRef, ON_SIGN.getValue());

        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

        PaymentResponse actualPayment = paymentProxyApiGateway.getPaymentOperation(paymentRef);
        checker.verifyPayment(request, actualPayment);

    }

    /* {
"error": "PERMISSION_DENIED",
"message": "User XAAAAA does not have CREATE_PAYMENT permission for organization UAAAAA"
}*/
    @Test
    @DisplayName("Создание платежа без права создания платежа")
    void checkCreatePaymentRule() {
        PaymentTestData testData = generate();

        ValidatableResponse response = paymentApiGateway.createPayment(testData.toPaymentRequest(42.00));
        checker.verifyErrorOperation(response, PERMISSION_DENIED, testData.permissionDeniedArgs("CREATE_PAYMENT"));

    }

    /* {
"error": "PERMISSION_DENIED",
"message": "User XAAAAA does not have SIGN_PAYMENT permission for organization UAAAAA"
}*/
    @Test
    @DisplayName("Подписание платежа без права подписания платежа")
    void checkSignPaymentRule() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT));

        PaymentRequest request = testData.toPaymentRequest(4200.00);

        String paymentRef = paymentApiGateway.getPaymentRef(request);

        ValidatableResponse signRef = signApiGateway.initSignBad(testData, paymentRef);

        checker.verifyErrorOperation(signRef, PERMISSION_DENIED, testData.permissionDeniedArgs("SIGN_PAYMENT"));
    }

    /* {
    "error": "INSUFFICIENT_FUNDS",
    "message": "Insufficient funds. Balance: 1000000.0, Amount: 4213441400.00"
}*/
    @Test
    @DisplayName("Создание платежа с суммой большей, чем баланс счета")
    void checkPaymentAmount() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
//        System.out.println("======== " + testData);

        double balance = accountsApiGateway.getAccountBalance(testData.getAccountNumber());
        double amount = balance + 15.00;

        ValidatableResponse response = paymentApiGateway.createPayment(testData.toPaymentRequest(amount));

        checker.verifyErrorOperation(response, INSUFFICIENT_FUNDS, balance, amount);
    }

    /* {
    "error": "ACCOUNT_ACCESS_DENIED",
    "message": "Account 40702810410805320705 does not belong to organization UAAAAA"
} */
    @Test
    @DisplayName("Создание платежа со счетом, который не принадлежит организации")
    void checkPaymentAccount() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));

        String acc = accountsApiGateway.createAccount(testUserDataApiGateway.getOrganizationId());

        ValidatableResponse response = paymentApiGateway.createPayment(testData.toPaymentRequestWithCustomAccountNumber(acc, 4500));
        checker.verifyErrorOperation(response, ACCOUNT_ACCESS_DENIED, acc, testData.getSenderOrganizationId());
    }

//    /* {
//    "error": "RECIPIENT_ORGANIZATION_NOT_FOUND",
//    "message": "Recipient organization not found: UXXXXX"
//}*/
    @Test
    @DisplayName("Создание платежа со получателем, которого не существует в системе")
    void checkPaymentRecipient() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));

        ValidatableResponse response = paymentApiGateway
            .createPayment(testData.toPaymentRequestWithCustomRecipientOrganizationId("UXXXXX", 4500.00));
        checker.verifyErrorOperation(response, RECIPIENT_ORGANIZATION_NOT_FOUND, testData.getRecipientOrganizationId());
    }
}
