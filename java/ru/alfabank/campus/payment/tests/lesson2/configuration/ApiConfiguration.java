package ru.alfabank.campus.payment.tests.lesson2.configuration;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiConfiguration {

    private static final String BASE_URI = "https://alfa-campus-qa.ru/automation";

    public static RequestSpecification getRequestSpec(String servicePath) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI + servicePath)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static final ResponseSpecification SUCCESS_RESPONSE_SPEC = new ResponseSpecBuilder()
            .log(LogDetail.BODY)
            .expectStatusCode(200)
            .build();
}
