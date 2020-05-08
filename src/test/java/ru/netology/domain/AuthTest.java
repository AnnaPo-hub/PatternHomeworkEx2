package ru.netology.domain;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    @Test
    void shouldRequestWithValidLoginInfo() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidActiveUser();
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Личный кабинет")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidLoginInfoButStatusBlocked() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidButBlockedUser();
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithoutPassword() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidActiveUser();
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(" ");
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithoutLogin() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidActiveUser();
        $("input[name =\"login\"]").setValue(" ");
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidPasswordButNotValidLogin() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidActiveUser();
        $("input[name =\"login\"]").setValue("Anna");
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidLoginButNotValidPassword() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateValidActiveUser();
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue("person");
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }

    @Test
    void shouldNotRequestWithValidLoginButWithoutRegistration() {
        open("http://localhost:9999");
        val person = PersonGenerator.Registration.generateUserWithoutRegistration();
        $("input[name =\"login\"]").setValue(person.getLogin());
        $("input[name=\"password\"]").setValue(person.getPassword());
        $("button[type=\"button\"][data-test-id=\"action-login\"]").click();
        $(withText("Ошибка")).waitUntil(Condition.visible, 5000);
    }
}
