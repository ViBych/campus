package ru.alfabank.campus.payment.tests.lesson1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {

    private String title;
    private String body;
    private int userId;
    private int id;

}
