package ru.alfabank.campus.test.lesson1;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson1.SingleUserResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseTests {

    @Test
    void test1() {
        JsonPath jsonPath = given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .jsonPath();

        int actualId = jsonPath.get("[0].id");
        List<String> emails = jsonPath.getList("email");

        assertEquals(1, actualId);
        assertEquals(10, emails.size());
    }

    @Test
    void test2() {
        Response response = given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();
        response.path("[0].id");
    }

    @Test
    void test3() {
        SingleUserResponse singleUserResponse = given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .as(SingleUserResponse.class);

        assertEquals(1, singleUserResponse.getId());
        assertEquals(1, singleUserResponse.getUserId());

        assertNotNull(singleUserResponse.getTitle());
        assertNotNull(singleUserResponse.getBody());
    }
}
