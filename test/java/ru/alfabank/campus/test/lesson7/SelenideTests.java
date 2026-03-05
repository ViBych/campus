package ru.alfabank.campus.test.lesson7;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SelenideTests {

    @Test
    void firstSelenideTest() throws InterruptedException {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "stable";
//        Configuration.browser = "firefox";

        Configuration.baseUrl = "https://alfabank.ru";
        Configuration.browserSize = "1920x1080";
//        Configuration.headless = true;
//        Configuration.holdBrowserOpen = true;


        Configuration.timeout = 10000;

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-features=LocalNetworkAccessChecks");

        Configuration.browserCapabilities = chromeOptions;


        Selenide.open("");
//        Thread.sleep(100000);


//        $(byXpath("//p[text() = 'Оформление онлайн']")).click();
        $(byText("Оформление онлайн")).click();
        $(byXpath("//h1[text() = 'Потребительские кредиты']")).shouldHave(Condition.text("Потребительские кредиты"));
        $(byXpath("(.//p[text()='Оформить заявку'])[1]")).click();

        $(byPlaceholder("Сумма")).setValue("3000000");

        $(byXpath(".//span[text() = 'Срок']")).click();
        $$("[data-test-id='form-select-term-option']").findBy(Condition.text("3 года")).click();

        $(byXpath(".//span[text() = 'Цель кредита']")).click();
        $$("[data-test-id='form-select-purpose-option']")
                .findBy(Condition.text("Ремонт дома/квартиры")).click();

        $(byName("fullName")).setValue("Иванов Иван Иванович");
        $(byName("phoneNumber")).setValue("9388883723");
        $(byName("email")).setValue("test@test.ru");

        if ($(byXpath(".//span[text() = 'Мужской']")).exists()) {
            $(byXpath(".//span[text() = 'Мужской']")).click();
        }

        $(byXpath(".//div[@data-test-id = 'sopdConfirmed-checkbox-area']")).click();

        $(byXpath(".//span[text() = 'Продолжить']")).click();
        $(byXpath(".//div[@data-test-id = 'sms-confirmation']")).shouldBe(visible);

//        Thread.sleep(100000);
    }

}
