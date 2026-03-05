package ru.alfabank.campus.test.lesson1;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SpecTests {

    private static RequestSpecification requestSpecification;
    private static ResponseSpecification successResponseSpecification;

    @BeforeAll
    static void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .addHeader("Custom header", "123321")
                .log(LogDetail.ALL)
                .build();

        successResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(Matchers.lessThan(10000L))
                .build();
    }

    @Test
    void test1() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/posts/1")
                .then()
                .spec(successResponseSpecification);
    }
}
