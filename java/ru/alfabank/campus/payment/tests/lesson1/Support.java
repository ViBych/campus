package ru.alfabank.campus.payment.tests.lesson1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Support {

    private String name;
    private String catchPhrase;
    private String bs;

}
