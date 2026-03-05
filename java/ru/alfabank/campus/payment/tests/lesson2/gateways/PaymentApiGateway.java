package ru.alfabank.campus.payment.tests.lesson2.gateways;

import io.restassured.response.ValidatableResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.payment.PaymentRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentErrorResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentResponse;

public class PaymentApiGateway extends BaseApiGateway {

    public PaymentApiGateway() {
        super("/payments-api");
    }

    public String getPaymentRef(PaymentRequest body) {
        return post(body, "/api/payments/create", PaymentResponse.class)
            .getReference();
    }

    public ValidatableResponse createPayment(PaymentRequest body) {
        return post(body, "/api/payments/create");
    }
}
