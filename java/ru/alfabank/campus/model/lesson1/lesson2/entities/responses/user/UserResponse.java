package ru.alfabank.campus.model.lesson1.lesson2.entities.responses.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String clientType;
}
