package ru.netology.domain;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpLogin() {
        given() // "дано"java -jar app-ibank.jar -P:profile=test
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(DataHelper.getAuthInfo()) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
     void checkStatus () throws SQLException {
     String status =  given() // "дано"java -jar app-ibank.jar -P:profile=test
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(DataHelper.getVerificationCode()) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                     .statusCode(200)
                .extract()
                    .path("status");
        System.out.println(status);
        assertThat(status, equalTo("ok"));
    }

}
