package ru.netology.domain;

import lombok.Value;

import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationData {
        private String login;
        private String code;
    }

    public static VerificationData getVerificationCode() throws SQLException {
        final String verificationCode = SqlUtils.getVerificationCode();
        return new VerificationData("vasya", verificationCode);
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
        private String cardBalance;
    }

    public static CardInfo getFirstCardInformation() {
        return new CardInfo("5559000000000001", "10000");
    }

    public static CardInfo getSecondCardInformation() {
        return new CardInfo("5559000000000002", "10000");
    }


}
