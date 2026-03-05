package ru.alfabank.campus.payment.tests.lesson2.utils;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.payment.PaymentRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentErrorResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentResponse;
import ru.alfabank.campus.payment.tests.lesson2.enums.PaymentsErrorMessage;
import ru.alfabank.campus.payment.tests.lesson2.gateways.PaymentProxyApiGateway;

public class OperationChecker {

    private final PaymentProxyApiGateway paymentProxyApiGateway = new PaymentProxyApiGateway();
    private final PaymentResponse paymentResponse = new PaymentResponse();
//    private final PaymentRequest paymentRequest = new PaymentRequest();

    public void verifyOperationStatus(String paymentRef, String expectedStatus) {
        String actualStatus = paymentProxyApiGateway.getPaymentOperationStatus(paymentRef);
        Assertions.assertEquals(expectedStatus, actualStatus, "Статус не соответствует ожидаемому");
    }

//    public void verifyErrorOperationStatus(ValidatableResponse response, String expectedErrorCode,
//                                           String expectedErrorMessage)
    public void verifyErrorOperation(ValidatableResponse response, PaymentsErrorMessage expected,
                                     Object... args) {

        PaymentErrorResponse errorResponse = response.extract().as(PaymentErrorResponse.class);
//        System.out.println(errorResponse);
        String expectedMessage = expected.format(args);

        Assertions.assertEquals(expected.getError(), errorResponse.getError(), () -> "Ожидалось: " + expected.getError() +
                ", но пришло: " + errorResponse.getError()
        );

        Assertions.assertEquals(expectedMessage, errorResponse.getMessage(), () -> "Ожидалось: "
            + expected.getMessage() + ", но пришло: " + errorResponse.getMessage()
        );
    }

    public void verifyPayment(PaymentRequest expected, PaymentResponse actual) {

        Assertions.assertEquals(expected.getFrom().getUserId(), actual.getFrom().getUserId(), "UserId отличается");

        Assertions.assertEquals(expected.getFrom().getOrganizationId(), actual.getFrom().getOrganizationId(),
            "OrganizationId отправителя отличается");

        Assertions.assertEquals(expected.getTo().getOrganizationId(), actual.getTo().getOrganizationId(),
            "OrganizationId получателя отличается");

        Assertions.assertEquals(expected.getFrom().getAccountNumber(), actual.getFrom().getAccountNumber(),
            "Номер счета отличается");

        Assertions.assertEquals(expected.getAmount(), actual.getAmount(), "Сумма платежа отличается");

    }

}
