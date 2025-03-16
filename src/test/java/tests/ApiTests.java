package tests;

import io.restassured.RestAssured;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static specs.ApiSpecs.*;
import static org.assertj.core.api.Assertions.assertThat;


public class ApiTests {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

        @Test
        @DisplayName("Успешное получение списка пользователей")
        void checkTotalValueTest () {

                    given(resourcesRequestSpec)
                            .when()
                            .queryParam("page", 2)
                            .get("/users")
                            .then()
                            .spec(response200Spec)
                            .body("total", is(12));
        }

    @Test
    @DisplayName("Создание пользователя")
    void createUserTest() {
        CreateUserModel newUser = new CreateUserModel();
        newUser.setName("morpheus");
        newUser.setJob("leader");

            CreateUserResponseModel response = step("Создание пользователя", () ->
                   given(resourcesRequestSpec)
                    .body(newUser)
                    .when()
                    .post("/users")
                    .then()
                    .spec(response201Spec)
                    .extract().as(CreateUserResponseModel.class)
    );

            step("Проверка ответа", () -> {
                    assertThat(response.getName()).isEqualTo("morpheus");
                    assertThat(response.getJob()).isEqualTo("leader");
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getCreatedAt()).isNotNull();
    });
}

    @Test
    @DisplayName("Обновить данные пользователя")

    void updateUserTest() {

        UpdateUserModel updateUser = new UpdateUserModel();
        updateUser.setName("morpheus");
        updateUser.setJob("zion resident");

        UpdateUserResponseModel response = step("Обновление пользователя", () ->
                given(resourcesRequestSpec)
                        .body(updateUser)
                        .when()
                        .queryParam("2")
                        .put("/users/")
                        .then()
                        .spec(response200Spec)
                        .extract().as(UpdateUserResponseModel.class)
        );

        step("Проверка ответа", () -> {
            assertThat(response.getName()).isEqualTo("morpheus");
            assertThat(response.getJob()).isEqualTo("zion resident");
            assertThat(response.getUpdatedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest(){

        given(resourcesRequestSpec)
                .when()
                .queryParam("2")
                .delete("/users/")
                .then()
                .spec(response204Spec);

    }

    @Test
    @DisplayName("Попытка авторизации без пароля")
    void missingPasswordTest(){
        LoginRequestModel request = new LoginRequestModel();
        request.setEmail("peter@klaven");

        ErrorResponseModel response = step("Попытка авторизации", () ->

                        given(resourcesRequestSpec)
                                .body(request)
                                .when()
                                .post("/login")
                                .then()
                                .spec(response400Spec)
                                .extract().as(ErrorResponseModel.class)
        );

        step("Проверка ответа", () -> {
            assertThat(response.getError()).isEqualTo("Missing password");
        });

    }
}
