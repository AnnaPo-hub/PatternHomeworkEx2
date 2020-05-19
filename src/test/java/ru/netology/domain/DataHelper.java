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
    public static class TransferAmountModel {
        private String from;
        private String to;
        private String amount;
    }

    public static TransferAmountModel getTransferAmountModel() {
        return new TransferAmountModel("5559000000000001", "5559000000000002", "5000");
    }
}
