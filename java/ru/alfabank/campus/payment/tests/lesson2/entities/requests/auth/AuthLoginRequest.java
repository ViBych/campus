package ru.alfabank.campus.payment.tests.lesson2.entities.requests.auth;

import lombok.Data;
import lombok.Getter;

@Data
public class AuthLoginRequest {

    private String login;
    private String password;

    public AuthLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
