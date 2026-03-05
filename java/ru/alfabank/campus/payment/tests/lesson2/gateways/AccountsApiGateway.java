package ru.alfabank.campus.payment.tests.lesson2.gateways;

import io.restassured.response.ValidatableResponse;
import ru.alfabank.campus.payment.tests.lesson2.entities.requests.account.AccountsRequest;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.account.AccountResponse;

import static io.restassured.RestAssured.given;

public class AccountsApiGateway extends BaseApiGateway {

    public AccountsApiGateway() {
        super("/accounts-api");
    }

    public String createAccount(AccountsRequest body) {
        return getAccountNumber(body);
    }

    public String createAccount(String organizationId) {
        AccountsRequest body = AccountsRequest.of(organizationId);
        return getAccountNumber(body);
    }

    private String getAccountNumber(AccountsRequest body) {
        return process(body).getAccountNumberFull();
    }

    private AccountResponse process(AccountsRequest body) {
        return post(body, "/api/accounts/create", AccountResponse.class);
    }

    public Double getAccountBalance(String accountNumber) {
        AccountResponse response = get("/api/accounts/balance?accountNumber=" + accountNumber, AccountResponse.class);
        return response.getBalance();
    }
}
