package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.responses.servicechecker.ServiceCheckerResponse;

import java.util.Map;

public class ServiceCheckerApiGateway extends BaseApiGateway {

    private final Map<String, String> headers;

    public ServiceCheckerApiGateway(String userId) {
        super("/api-service-checker");
        headers = Map.of("X-User-Id", userId);
    }

    public ServiceCheckerResponse test() {
        return get("/service/test", headers, ServiceCheckerResponse.class);
    }
}
