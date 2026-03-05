package ru.alfabank.campus.payment.tests.lesson1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String title;
    private String body;
    private int userId;

}
