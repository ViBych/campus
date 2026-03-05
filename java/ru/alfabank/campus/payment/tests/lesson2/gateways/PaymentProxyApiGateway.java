package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentProxyResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.payment.PaymentResponse;

public class PaymentProxyApiGateway extends BaseApiGateway {

    public PaymentProxyApiGateway() {
        super("/payment-proxy-api");
    }

    public String getPaymentOperationStatus(String paymentRef) {
        return get("/api/payment-proxy/" + paymentRef, PaymentResponse.class)
                .getStatus();
    }
    public PaymentResponse getPaymentOperation(String paymentRef) {
        return get("/api/payment-proxy/" + paymentRef, PaymentResponse.class);
    }
}
