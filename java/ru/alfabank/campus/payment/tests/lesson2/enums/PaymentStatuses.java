package ru.alfabank.campus.payment.tests.lesson2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatuses {

    NEW("N"),
    ON_SIGN("C"),
    SUCCESS("S"),
    FAILED("F"),
    BLOCKED("M");

    private final String value;
}
