package ru.alfabank.campus.test.lesson6.part1;

import customers.TestCustomerData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.auth.AuthLoginRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.complaince.ComplianceCheckResponse;
import ru.alfabank.campus.payment.tests.lesson2.gateways.AuthApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.ComplianceApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.PaymentApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.gateways.SignApiGateway;
import ru.alfabank.campus.payment.tests.lesson2.utils.AuthSession;
import ru.alfabank.campus.payment.tests.lesson2.utils.OperationChecker;
import ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData;

import java.util.List;

import static ru.alfabank.campus.payment.tests.lesson2.enums.PaymentStatuses.*;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.CREATE_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.enums.UserRights.SIGN_PAYMENT;
import static ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData.generate;

public class ComplianceTests {

    private final PaymentApiGateway paymentApiGateway = new PaymentApiGateway();
    private final OperationChecker checker = new OperationChecker();
    private final SignApiGateway signApiGateway = new SignApiGateway();
    private final AuthApiGateway authApiGateway = new AuthApiGateway();
    private final ComplianceApiGateway complianceApiGateway = new ComplianceApiGateway();
    final double MAX_PAYMENT = 600000.00;

    @Test
    @DisplayName("Проверка комплаенс проходит успешно")
    void successfulComplianceCheckForSmallPayment() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT - 100));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

        AuthSession session = new AuthSession();
        String token = session.getToken();

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(SUCCESS.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

    }

    @Test
    @DisplayName("Платёж блокируется при сумме более 600 000")
    void paymentBlockedWhenAmountExceedsThreshold() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT + 1));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

        AuthSession session = new AuthSession();
        String token = session.getToken();

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(BLOCKED.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("NOT_OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, BLOCKED.getValue());
    }

    @Test
    @DisplayName("Платёж не блокируется при сумме равной 600 000")
    void paymentNotBlockedWhenAmountIsEqualToLimit() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

        AuthSession session = new AuthSession();
        String token = session.getToken();

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(SUCCESS.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
    }

    @Test
    @DisplayName("Платёж не блокируется при достижении дневного лимита 600 000")
    void paymentNotBlockedWhenDailySumIsEqualToLimit() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT - 100));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
        System.out.println("==========");
        System.out.println("Первый платеж на " + (MAX_PAYMENT - 100) + " прошел");
        System.out.println("==========");

        String paymentRef2 = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(100));
        String signRef2 = signApiGateway.initSign(testData, paymentRef2);
        signApiGateway.confirmSign(testData, signRef2);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
        System.out.println("==========");
        System.out.println("2 платеж на " + 100 + " прошел");
        System.out.println("==========");

        AuthSession session = new AuthSession();
        String token = session.getToken();

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(SUCCESS.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
    }

    @Test
    @DisplayName("Платёж блокируется при превышении дневного лимита 600 000")
    void paymentBlockedWhenDailySumExceedsThreshold() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));
        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT - 100));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
        System.out.println("==========");
        System.out.println("Первый платеж на " + (MAX_PAYMENT - 100) + " прошел");
        System.out.println("==========");

        String paymentRef2 = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(101));
        String signRef2 = signApiGateway.initSign(testData, paymentRef2);
        signApiGateway.confirmSign(testData, signRef2);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
        System.out.println("==========");
        System.out.println("2 платеж на " + 101 + " не прошел");
        System.out.println("==========");

        AuthSession session = new AuthSession();
        String token = session.getToken();

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(BLOCKED.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("NOT_OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, BLOCKED.getValue());
    }

    @Test
    @DisplayName("Платёж блокируется при отправке подозрительной организации")
    void paymentBlockedWhenRecipientIsSuspicious() {
        PaymentTestData testData = generate()
            .addRights(List.of(CREATE_PAYMENT, SIGN_PAYMENT));

        System.out.println("============ генерация ============");

        AuthSession session = new AuthSession();
        String token = session.getToken();

        System.out.println("============ токен сессии ============");

        String orgRecipientId = testData.getRecipientOrganizationId();
        complianceApiGateway.doOrgSuspicious(orgRecipientId, token);

        System.out.println("============ сделали подозрительной организацию ============");

        String paymentRef = paymentApiGateway.getPaymentRef(testData.toPaymentRequest(MAX_PAYMENT - 100));
        String signRef = signApiGateway.initSign(testData, paymentRef);
        signApiGateway.confirmSign(testData, signRef);
        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());

        ComplianceCheckResponse complianceResponse = complianceApiGateway.complianceCheck(token, paymentRef);

        Assertions.assertEquals(SUCCESS.getValue(), complianceResponse.getPaymentStatus());
        Assertions.assertEquals("OK", complianceResponse.getResult());

        checker.verifyOperationStatus(paymentRef, SUCCESS.getValue());
    }
}
