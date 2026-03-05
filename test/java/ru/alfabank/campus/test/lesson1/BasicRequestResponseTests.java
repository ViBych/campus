package ru.alfabank.campus.test.lesson1;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class BasicRequestResponseTests {

//    https://jsonplaceholder.typicode.com/
    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @Test
    void test() {
        given()
            .baseUri("https://jsonplaceholder.typicode.com")
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("title", notNullValue())
            .log().all();

    }

    @Test
    void test2() {
        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .queryParam("userId", 1)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Custom-header", "123")
                .log().all()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("$", hasSize(10))
                .log().all();
    }

    @Test
    void test3() {
        String requestBody = """
                {
                    "title": "campus post",
                    "body": "test body campus post",
                    "userId": 1
                }
                """;

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("campus post"))
            .body("body", equalTo("test body campus post"))
            .body("userId", equalTo(1))
            .log().all();
    }

    @Test
    void test4() {
        String requestBody = """
                {
                    "title": "campus post",
                    "body": "updated test body campus post",
                    "userId": 1
                }
                """;

        given()
                .baseUri(BASE_URI)
                .contentType("application/json")
                .pathParam("postId", 4)
                .body(requestBody)
                .log().all()
        .when()
                .put("/posts/{postId}")
        .then()
                .statusCode(200);
    }
}
