package ru.alfabank.campus.model.lesson1.lesson2.gateways;

import io.restassured.RestAssured;
import ru.alfabank.campus.model.lesson1.lesson2.entities.responses.user.UserResponse;

public class TestUserDataApiGateway {

    private static final String BASE_URI = "https://alfa-campus-qa.ru/automation/test-user-data-api/api";

//    public String getUserId() {
////
//        String user = RestAssured.given()
//            .baseUri(BASE_URI)
//            .when()
//            .post("/users/generate")
//            .then()
//            .statusCode(200)
//            .extract()
//            .response()
//            .as(UserResponse.class);
//    }
}