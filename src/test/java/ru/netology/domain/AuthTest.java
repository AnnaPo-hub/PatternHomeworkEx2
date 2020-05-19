package ru.netology.domain;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

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

    }

    @Test
    void viewCards (){
    Response response = given()
               .headers("Authorization", "Bearer "+token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .spec(requestSpec)
                .when()
                .get("/api/cards")
                .then().log().all()
             .statusCode(200)
               .extract()
               .response();
        System.out.println(response);
        assertThat(response, notNullValue());
    }

    @Test
    void transfer (){
       Response response =  given()
                .headers("Authorization", "Bearer "+token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .spec(requestSpec)
                .body("\"from\": \"5559 0000 0000 0002\",\n" +
                        "  \"to\": \"5559 0000 0000 0001\",\n" +
                        "  \"amount\": \"5000\"")
                .when()
                .post("/api/transfer")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
        System.out.println(response);
    }

//    @AfterAll
//    static void close() throws SQLException {
//        SqlUtils.cleanDb();
//    }

}
