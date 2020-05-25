package ru.netology.domain.test;


import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.domain.restApi.RestApiHelper;
import ru.netology.domain.sqlUtils.SqlUtils;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {

    private int amountToTransfer = 5000;
    private static String cardNumberFrom = "5559 0000 0000 0001";
    private static String cardNumberTo = "5559 0000 0000 0002";

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

    public static TransferAmountModel getTransferAmountModel(String amountToTransfer) {
        return new TransferAmountModel(cardNumberFrom, cardNumberTo, amountToTransfer);
    }

    @Value
    public static class TransferAmountModel {
        private String from;
        private String to;
        private String amount;
    }

    @BeforeAll
    static void setUp() {
        val loginInfo = getAuthInfo();
        RestApiHelper.login(loginInfo);
    }

    @Test
    void shouldCheckTransfer() throws SQLException {
        final int balanceOfCardToExpected = checkBalanceOnCardToShouldBe(cardNumberTo, amountToTransfer);
        final int balanceOfCardFromExpected = checkBalanceOnCardFromShouldBe(cardNumberFrom, amountToTransfer);
        val verificationData = SqlUtils.getVerificationCode(getAuthInfo().login);
        final String token = RestApiHelper.checkStatusAndExtractToken(verificationData);
        val transferModel = getTransferAmountModel(Integer.toString(amountToTransfer));
        RestApiHelper.transfer(token, transferModel);
        int balanceOfCardFromActual = SqlUtils.getBalance(cardNumberFrom);
        int balanceOfCardToActual = SqlUtils.getBalance(cardNumberTo);
        final String transaction = SqlUtils.findTransaction(cardNumberTo);
        assertThat(transaction, notNullValue());
        assertEquals(balanceOfCardFromExpected, balanceOfCardFromActual);
        assertEquals(balanceOfCardToExpected, balanceOfCardToActual);
    }

    @Test
    void viewCards() throws SQLException {
        val verificationData = SqlUtils.getVerificationCode(getAuthInfo().login);
        final String token = RestApiHelper.checkStatusAndExtractToken(verificationData);
        RestApiHelper.viewCards(token);
    }

    @AfterAll
    static void close() throws SQLException {
        SqlUtils.cleanDb();
    }

    public static int checkBalanceOnCardFromShouldBe(String cardNumberFrom, int amountToTransfer) throws SQLException {
        final int initialBalanceFrom = SqlUtils.getBalance(cardNumberFrom);
        return initialBalanceFrom - amountToTransfer;
    }

    public static int checkBalanceOnCardToShouldBe(String cardNumberTo, int amountToTransfer) throws SQLException {
        final int initialBalanceTo = SqlUtils.getBalance(cardNumberTo);
        return initialBalanceTo + amountToTransfer;
    }
}
