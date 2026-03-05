package ru.alfabank.campus.payment.tests.lesson2.gateways;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.alfabank.campus.payment.tests.lesson2.configuration.ApiConfiguration;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class BaseApiGateway {

    private final RequestSpecification requestSpec;
    private final ResponseSpecification successResponseSpec;

    public BaseApiGateway(String servicePath) {
        requestSpec = ApiConfiguration.getRequestSpec(servicePath);
        successResponseSpec = ApiConfiguration.SUCCESS_RESPONSE_SPEC;
    }


    public <T> T post(Object body, String path, Class<T> responseType) {
        return extract(
                given(requestSpec)
                        .body(body)
                        .when()
                        .post(path), responseType
        );
    }

    public <T> T post(String path, Class<T> responseType) {
        return extract(
                given(requestSpec)
                        .when()
                        .post(path), responseType
        );
    }

    public <T> T get(String path, Class<T> responseType) {
        return extract(
                given(requestSpec)
                        .when()
                        .get(path), responseType
        );
    }

    public <T> T get(String path, Map<String, String> headers, Class<T> responseType) {
        var spec = given(requestSpec);
        headers.forEach(spec::header);
        return extract(
                spec
                    .when()
                    .get(path), responseType
        );
    }

    public ValidatableResponse post(Object body, String path) {
        return RestAssured.given(requestSpec)
                .body(body)
                .when()
                .post(path)
                .then();
    }

    public <T> T put(String path, Map<String, String> headers,
                     Class<T> responseType) {
        var spec = given(requestSpec);
        headers.forEach(spec::header);
        return extract(
            spec
                .when()
                .put(path),
            responseType
        );
    }

    private <T> T extract(Response response, Class<T> responseType) {
        return response
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(responseType);
    }


}
