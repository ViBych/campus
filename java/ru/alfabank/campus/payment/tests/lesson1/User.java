package ru.alfabank.campus.payment.tests.lesson1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;

}
