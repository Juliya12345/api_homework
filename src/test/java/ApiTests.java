import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    void checkTotalValue (){
        given()
                .log().uri()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12))
                .body("data", hasSize(greaterThan(0)));

    }
    @Test
    void createUser() {

        String data = "{\"name\": \"morpheus\", \"job\": \"leader\"}";

        given()
                .body(data)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue());

    }
    @Test
    void updateUser() {

        String data = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
                .body(data)
                .contentType(JSON)
                .log().uri()
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", equalTo("zion resident"));
    }

    @Test
    void deleteUser(){
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
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
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }
}
