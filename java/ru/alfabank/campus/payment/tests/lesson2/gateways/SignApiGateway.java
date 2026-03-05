package ru.alfabank.campus.payment.tests.lesson2.gateways;

import io.restassured.response.ValidatableResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.sign.ConfirmSignRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.sign.InitSignRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.sign.SignResponse;
import ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData;

import static ru.alfabank.campus.payment.tests.lesson2.entities.requests.sign.InitSignRequest.*;

public class SignApiGateway extends BaseApiGateway{

    public SignApiGateway() {
        super("/sign-api");
    }

    public String initSign(String userId, String organizationId, String paymentReference) {
        InitSignRequest body = of(userId, organizationId, paymentReference);
        return post(body, "/api/sign/init", SignResponse.class)
                .getSignReference();
    }

    public String initSign(PaymentTestData testData, String paymentReference) {
        InitSignRequest body = of(testData.getUserId(), testData.getSenderOrganizationId(), paymentReference);
        return post(body, "/api/sign/init", SignResponse.class)
                .getSignReference();
    }

    public ValidatableResponse initSignBad(PaymentTestData testData, String paymentReference) {
        InitSignRequest body = of(testData.getUserId(), testData.getSenderOrganizationId(), paymentReference);
        return post(body, "/api/sign/init");
    }

    public void confirmSign(String userId, String organizationId, String signReference) {
        ConfirmSignRequest body = ConfirmSignRequest.of(userId, organizationId, signReference);
        post(body, "/api/sign/confirm");
    }

    public void confirmSign(PaymentTestData testData, String signReference) {
        ConfirmSignRequest body = ConfirmSignRequest.of(testData.getUserId(),
                testData.getSenderOrganizationId(), signReference);
        post(body, "/api/sign/confirm");
    }
}
