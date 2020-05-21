package ru.netology.domain.restApi;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.domain.data.DataHelper;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class RestApiHelper {

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static String token;

    public static void login() {
        given() // -P:profile=test
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(DataHelper.getAuthInfo()) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String checkStatusAndExtractToken() throws SQLException {
        token = given()
                .spec(requestSpec)
                .body(DataHelper.getVerificationCode())
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        return token;
    }

    public static void transfer() throws SQLException {
        given()
                .headers("Authorization", "Bearer " + token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .spec(requestSpec)
                .body(DataHelper.getTransferAmountModel())
                .when()
                .post("/api/transfer")
                .then().log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    public static void viewCards() {
        given()
                .headers("Authorization", "Bearer " + token, "Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .spec(requestSpec)
                .when()
                .get("/api/cards")
                .then().log().all()
                .statusCode(200);
    }
}
