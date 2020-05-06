package ru.netology.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegistrationData {
    private String login;
    private String password;
    private Status status;

    public RegistrationData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RegistrationData(String login, String password, Status status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public RegistrationData(String password, Status status) {
        this.password = password;
        this.status = status;
    }
}