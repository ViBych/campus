package ru.alfabank.campus.test.lesson8;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson8.entities.pages.CreatePaymentPage;
import ru.alfabank.campus.payment.tests.lesson8.entities.pages.LoginPage;
import ru.alfabank.campus.payment.tests.lesson8.utils.TestUser;
import ru.alfabank.campus.payment.tests.lesson8.utils.TestUserGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

public class PaymentUITests {

    @BeforeAll
    static void init() {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "stable";
        Configuration.baseUrl = "https://alfabank.ru/automation/campus-payment";
        Configuration.browserSize = "1920x1080";
    }

    @Test
    void loginTest() throws InterruptedException {
        TestUser user = new TestUserGenerator().generate().build();
        LoginPage loginPage = LoginPage.open()
            .enterXPin(user.getUserId())
            .enterPassportData(user.getPassportSeries(), user.getPassportNumber());
        CreatePaymentPage paymentPage = loginPage.enterSmsCode();


//        //1
//        Selenide.open("/login");
//        $("[data-test-id='login-xpin-input']").setValue(user.getUserId());
//        $("[data-test-id='login-xpin-submit']").click();
//
//        //2
//        $("[data-test-id='login-passport-series-input']").setValue(user.getPassportSeries());
//        $("[data-test-id='login-passport-number-input']").setValue(user.getPassportNumber());
//        $("[data-test-id='login-passport-submit']").click();
//
//        //3
//        $(byXpath("///input[@autocomplete='one-time-code']")).setValue("000000");
//
//        $(byXpath("[data-test-id='nav-link-all-payments]")).shouldHave(text("Все платежи"));
//        $(byXpath("[data-test-id='nav-link-all-create-payment]")).shouldHave(text("Создать платёж"));

    }


}
