package ru.alfabank.campus.test.lesson2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaymentCreationTests {

    @Test
    @DisplayName("Проверка создания платежа")
    void shouldCreatePayment() {
        String userId = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/test-user-data-api/api")
                .when()
                .post("/users/generate")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("id");

        String senderOrganizationId = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/test-user-data-api/api")
                .when()
                .post("/organizations/generate")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("organizationId");

        String recipientOrganizationId = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/test-user-data-api/api")
                .when()
                .post("/organizations/generate")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("organizationId");


        String accountBodyRequest = String.format("""
                {
                    "uPin": "%s",
                    "currency": "RUR",
                    "accountInfo": "PAYMENT_RUR"
                  }
                """, senderOrganizationId);

        String accountNumber = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/accounts-api/api/accounts")
                .contentType(ContentType.JSON)
                .body(accountBodyRequest)
                .when()
                .post("/create")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("accountNumberFull");

        String rightRequest = """
                {
                    "xPin": "%s",
                    "uPin": "%s",
                    "rule": "CREATE_PAYMENT"
                  }
                """.formatted(userId, senderOrganizationId);

        RestAssured
                .given()
                .body(rightRequest)
                .contentType(ContentType.JSON)
                .post("https://alfa-campus-qa.ru/automation/roles-api/api/roles/add")
                .then()
                .statusCode(200);

        String rightSignRequest = """
                {
                    "xPin": "%s",
                    "uPin": "%s",
                    "rule": "SIGN_PAYMENT"
                  }
                """.formatted(userId, senderOrganizationId);

        RestAssured
                .given()
                .body(rightSignRequest)
                .contentType(ContentType.JSON)
                .post("https://alfa-campus-qa.ru/automation/roles-api/api/roles/add")
                .then()
                .statusCode(200);

        String paymentBodyRequest = String.format("""
                {
                    "from": {
                        "userId": "%s",
                        "organizationId": "%s",
                        "accountNumber": "%s"
                    },
                    "to": {
                        "organizationId": "%s"
                    },
                    "amount": 4200.00
                  }
                """, userId, senderOrganizationId, accountNumber, recipientOrganizationId);

        String paymentRef = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/payments-api/api/payments")
                .body(paymentBodyRequest)
                .contentType(ContentType.JSON)
                .when()
                .post("/create")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response()
                .path("reference");

        String signBodyReq = """
                {
                    "xPin": "%s",
                    "uPin": "%s",
                    "paymentReference": "%s"
                  }
                """.formatted(userId, senderOrganizationId, paymentRef);

        String signRef = RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/sign-api/api/sign")
                .body(signBodyReq)
                .contentType(ContentType.JSON)
                .when()
                .post("/init")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response()
                .path("signReference");

        String confirmBodyReq = """
                {
                    "xPin": "%s",
                    "uPin": "%s",
                    "signReference": "%s"
                  }
                """.formatted(userId, senderOrganizationId, signRef);

        RestAssured.given()
                .baseUri("https://alfa-campus-qa.ru/automation/sign-api/api/sign")
                .body(confirmBodyReq)
                .contentType(ContentType.JSON)
                .when()
                .post("/confirm")
                .then()
                .statusCode(200)
                .log().all();

        RestAssured.get("https://alfa-campus-qa.ru/automation/payment-proxy-api/api/payment-proxy/" + paymentRef)
                .then()
                .log().all();
    }
}
