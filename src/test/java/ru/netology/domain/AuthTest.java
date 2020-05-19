package ru.netology.domain;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class AuthTest {
    private static String token;
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpLogin() {
        given() // -P:profile=test
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(DataHelper.getAuthInfo()) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @BeforeAll
    static void checkStatusAndExtractToken() throws SQLException {
     token =  given()
                .spec(requestSpec)
                .body(DataHelper.getVerificationCode())
                .when()
                .post("/api/auth/verification")
                .then()
                     .statusCode(200)
                .extract()
                    .path("token");
        System.out.println(token);
    }

    @Test
    void viewCards (){
     given()
               .headers("Authorization", "Bearer "+token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .spec(requestSpec)
                .when()
                .get("/api/cards")
                .then()
             .statusCode(200)
               .extract()
               .response();
    }

//    @Test
//    void transfer (){
//        given()
//                .headers("Authorization", "Bearer "+token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
//                .spec(requestSpec)
//                .body("\"from\": \"5559 0000 0000 0002\",\n" +
//                        "  \"to\": \"5559 0000 0000 0008\",\n" +
//                        "  \"amount\": 5000")
//                .when()
//                .post("/api/transfer")
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//    }

    @AfterAll
    static void close() throws SQLException {
        SqlUtils.cleanDb();
    }

}
