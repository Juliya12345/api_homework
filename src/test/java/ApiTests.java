import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

public class ApiTests {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void checkTotalValueTest (){
        given()
                .log().uri()
                .queryParam("page", 2)
                .get("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12))
                .body("data", hasSize(greaterThan(0)));

    }
    @Test
    void createUserTest() {

        String data = "{\"name\": \"morpheus\", \"job\": \"leader\"}";

        given()
                .body(data)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue());

    }
    @Test
    void updateUserTest() {

        String data = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
                .body(data)
                .contentType(JSON)
                .log().uri()
                .when()
                .queryParam("2")
                .put("/users/")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", equalTo("zion resident"));
    }

    @Test
    void deleteUserTest(){
        given()
                .when()
                .queryParam("2")
                .delete("/users/")
                .then()
                .statusCode(204);
    }

    @Test
    void wrongBodyTest() {

        String data = "sssssss";

        given()
                .body(data)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }
}
