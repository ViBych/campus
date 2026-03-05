package ru.alfabank.campus.payment.tests.lesson2.utils;

import customers.TestCustomerData;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.auth.AuthLoginRequest;
import ru.alfabank.campus.payment.tests.lesson2.gateways.AuthApiGateway;

public class AuthSession {

    private final AuthApiGateway authApiGateway = new AuthApiGateway();
    private String token;

    public String getToken() {
        if (token == null) {
            AuthLoginRequest request = new AuthLoginRequest(TestCustomerData.LOGIN, TestCustomerData.PASSWORD);
            token = authApiGateway.login(request);
        }
        return token;
    }
}
