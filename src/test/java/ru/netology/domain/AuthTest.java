package ru.netology.domain;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static ru.netology.domain.PersonGenerator.Registration.generate;

public class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

//    @BeforeAll
//    static void setUpAll() {
//        // сам запрос
//        given() // "дано"java -jar app-ibank.jar -P:profile=test
//                .spec(requestSpec) // указываем, какую спецификацию используем
//                .body(new RegistrationData(generate("en").getLogin(), generate("en").getPassword(), Status.ACTIVE)) // передаём в теле объект, который будет преобразован в JSON
//                .when() // "когда"
//                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
//                .then() // "тогда ожидаем"
//                .statusCode(200); // код 200 OK
//    }

    @Test
    void shouldRequestWithValidData() {
        given()
                .spec(requestSpec)
                .body(new RegistrationData("vasya", "password"))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotRequestWithNotValidData() {
        given()
                .spec(requestSpec)
                .body(new RegistrationData(generate("en").getLogin(), generate("en").getPassword(), Status.BLOCKED))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500); //
    }

    @Test
    void shouldNotRequestWithoutLogin() {
        given()
                .spec(requestSpec)
                .body(new RegistrationData(null, generate("en").getPassword(), Status.ACTIVE))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);
    }

    @Test
    void shouldNotRequestWithoutPassword() {
        given() //
                .spec(requestSpec)
                .body(new RegistrationData(generate("en").getLogin(), null,  Status.BLOCKED))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500);


    }
}
