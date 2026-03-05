package ru.alfabank.campus.payment.tests.lesson8.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TestUser {

    //Пользователь (физлицо)
    private String userId;
    private String firstName;
    private String lastName;
    private String passportSeries;
    private String passportNumber;

    //Отправитель
    private String senderOrgId;
    private String senderOrgName;

    //Получатель
    private String recipientOrgId;
    private String recipientOrgName;

    //Счета
    private List<String> accountNumbers;


}
