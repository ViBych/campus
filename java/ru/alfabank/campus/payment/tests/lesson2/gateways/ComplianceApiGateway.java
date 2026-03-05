package ru.alfabank.campus.payment.tests.lesson2.gateways;

import ru.alfabank.campus.payment.tests.lesson2.entities.responses.complaince.ComplianceCheckResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.complaince.SuspiciousResponse;

import java.util.Map;

public class ComplianceApiGateway extends BaseApiGateway {
    public ComplianceApiGateway() {
        super("/compliance-api");
    }

    public ComplianceCheckResponse complianceCheck(String authHeader, String paymentRef) {
        return get("/api/compliance/check/" + paymentRef, Map.of("Authorization", authHeader),
                ComplianceCheckResponse.class);
    }

    public SuspiciousResponse doOrgSuspicious(String id, String authToken) {
        return put("/api/compliance/suspicious/" + id, Map.of("Authorization", authToken),
            SuspiciousResponse.class);
    }
}
