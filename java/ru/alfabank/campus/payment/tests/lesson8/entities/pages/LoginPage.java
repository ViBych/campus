package ru.alfabank.campus.payment.tests.lesson8.entities.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement xPinInput = $("[data-test-id='login-xpin-input']");
    private final SelenideElement xPinSubmitBtn = $("[data-test-id='login-xpin-submit']");

    private final SelenideElement passportSeriesInput = $("[data-test-id='login-passport-series-input]");
    private final SelenideElement passportNumberInput = $("[data-test-id='login-passport-number-input]");
    private final SelenideElement passportSubmit = $("[data-test-id='login-passport-submit]");


    private final SelenideElement smsCodeInput = $(byXpath("///input[@autocomplete='one-time-code']"));

    public static LoginPage open() {
        Selenide.open("/login");
        return new LoginPage();
    }

    public LoginPage enterXPin(String xPin) {
        xPinInput.setValue(xPin);
        xPinSubmitBtn.click();
        return this;
    }

    public LoginPage enterPassportData(String series, String number) {
        passportSeriesInput.setValue(series);
        passportNumberInput.setValue(series);
        passportSubmit.click();
        return this;
    }

    public CreatePaymentPage enterSmsCode() {
        smsCodeInput.setValue("000000");
        return new CreatePaymentPage();
    }
}

//    Selenide.open("/login");
//    $("[data-test-id='login-xpin-input']").setValue(user.getUserId());
//    $("[data-test-id='login-xpin-submit']").click();
//
//    //2
//    $("[data-test-id='login-passport-series-input']").setValue(user.getPassportSeries());
//    $("[data-test-id='login-passport-number-input']").setValue(user.getPassportNumber());
//    $("[data-test-id='login-passport-submit']").click();
//
//    //3
//    $(byXpath("///input[@autocomplete='one-time-code']")).setValue("000000");
//
//    $(byXpath("[data-test-id='nav-link-all-payments]")).shouldHave(text("Все платежи"));
//    $(byXpath("[data-test-id='nav-link-all-create-payment]")).shouldHave(text("Создать платёж"));