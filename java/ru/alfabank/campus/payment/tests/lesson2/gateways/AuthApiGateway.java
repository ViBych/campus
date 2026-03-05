package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.requests.auth.AuthLoginRequest;

public class AuthApiGateway extends BaseApiGateway {
    public AuthApiGateway() {
        super("/auth-api");
    }

    public String login(AuthLoginRequest body) {
        return post(body, "/api/auth/login")
                .extract()
                .header("Authorization");
    }
}
