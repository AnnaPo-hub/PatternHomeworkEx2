package ru.netology.domain.data;

import lombok.Value;
import ru.netology.domain.sqlUtils.SqlUtils;

import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationData getVerificationCode() throws SQLException {
        final String verificationCode = SqlUtils.getVerificationCode();
        return new VerificationData("vasya", verificationCode);
    }

    public static TransferAmountModel getTransferAmountModel() {
        return new TransferAmountModel("5559 0000 0000 0001", "5559 0000 0000 0002", "5000");
    }

    public static int checkBalanceOnCardFromShouldBe(String cardNumber, int amountToTransfer) throws SQLException {
        final int initialBalanceFrom = SqlUtils.getBalance("5559 0000 0000 0001");
        return initialBalanceFrom - amountToTransfer;
    }

    public static int checkBalanceOnCardToShouldBe(String cardNumber, int amountToTransfer) throws SQLException {
        final int initialBalanceTo = SqlUtils.getBalance("5559 0000 0000 0002");
        return initialBalanceTo + amountToTransfer;
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    @Value
    public static class VerificationData {
        private String login;
        private String code;
    }

    @Value
    public static class TransferAmountModel {
        private String from;
        private String to;
        private String amount;
    }
}
