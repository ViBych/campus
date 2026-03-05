package ru.alfabank.campus.payment.tests.lesson2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum PaymentsErrorMessage {

    PERMISSION_DENIED("PERMISSION_DENIED",
        "User %s does not have %s permission for organization %s"),

    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS",
        "Insufficient funds. Balance: %s, Amount: %s"),

    ACCOUNT_ACCESS_DENIED("ACCOUNT_ACCESS_DENIED",
        "Account %s does not belong to organization %s"),

    RECIPIENT_ORGANIZATION_NOT_FOUND("RECIPIENT_ORGANIZATION_NOT_FOUND",
        "Recipient organization not found: %s");

    private final String error;
    private final String message;

    PaymentsErrorMessage(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String format(Object[] args) {
        return String.format(message, args);
    }
}
