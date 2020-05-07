package ru.netology.domain;

import com.codeborne.selenide.Condition;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class AuthTest {
    private static RegistrationData person;

        private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {
        person = PersonGenerator.Registration.generate("en");
        given() // "дано"java -jar app-ibank.jar -P:profile=test
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(person) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
    void shouldRequestOrNotWithValidDataDependingOnStatus() {
        open("http://localhost:9999");
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        if (person.getStatus().equals(Status.ACTIVE)) {
            $(withText("Личный кабинет")).waitUntil(Condition.visible, 5000);
        } else if ((person.getStatus().equals(Status.BLOCKED))) {
            $("[data-test-id =\"error-notification\"]").waitUntil(Condition.visible, 5000);
        }
    }

    @Test
    void shouldNotRequestWithoutPassword() {
        open("http://localhost:9999");
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(" ");
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithoutLogin() {
        open("http://localhost:9999");
        $("input[name =\"login\"]").setValue(" ");
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidPasswordButNotValidLogin() {
        open("http://localhost:9999");
        $("input[name =\"login\"]").setValue("Anna");
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidLoginButNotValidPassword() {
        open("http://localhost:9999");
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue("person");
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }
}



