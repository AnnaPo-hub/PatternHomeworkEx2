package ru.netology.domain.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.domain.data.DataHelper;
import ru.netology.domain.restApi.RestApiHelper;
import ru.netology.domain.sqlUtils.SqlUtils;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {
    private int amountToTransfer = 5000;

    @BeforeAll
    static void setUp() throws SQLException {
        RestApiHelper.login();
        final String token = RestApiHelper.checkStatusAndExtractToken();
        assertThat(token, notNullValue());
    }

    @Test
    void shouldCheckTransfer() throws SQLException {
        final int balanceOfCardToExpected = DataHelper.checkBalanceOnCardToShouldBe("5559 0000 0000 0002", amountToTransfer);
        final int balanceOfCardFromExpected = DataHelper.checkBalanceOnCardFromShouldBe("5559 0000 0000 0001", amountToTransfer);
        RestApiHelper.transfer();
        int balanceOfCardFromActual = SqlUtils.getBalance("5559 0000 0000 0001");
        int balanceOfCardToActual = SqlUtils.getBalance("5559 0000 0000 0002");
        final String transaction = SqlUtils.findTransaction();
        assertThat(transaction, notNullValue());
        assertEquals(balanceOfCardFromExpected, balanceOfCardFromActual);
        assertEquals(balanceOfCardToExpected, balanceOfCardToActual);
    }

    @Test
    void viewCards() {
        RestApiHelper.viewCards();
    }

//    @AfterAll
//    static void close() throws SQLException {
//        SqlUtils.cleanDb();
//    }
}
