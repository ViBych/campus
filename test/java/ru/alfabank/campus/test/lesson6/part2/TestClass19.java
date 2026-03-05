package ru.alfabank.campus.test.lesson6.part2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.servicechecker.ServiceCheckerResponse;
import ru.alfabank.campus.payment.tests.lesson2.gateways.ServiceCheckerApiGateway;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестовый класс 19")
public class TestClass19 {

    private final ServiceCheckerApiGateway gateway = new ServiceCheckerApiGateway("");

    @Test
    @DisplayName("Тест №1 класса [19]")
    void test1() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №2 класса [19]")
    void test2() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №3 класса [19]")
    void test3() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №4 класса [19]")
    void test4() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №5 класса [19]")
    void test5() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №6 класса [19]")
    void test6() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №7 класса [19]")
    void test7() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №8 класса [19]")
    void test8() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №9 класса [19]")
    void test9() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }

    @Test
    @DisplayName("Тест №10 класса [19]")
    void test10() {
        ServiceCheckerResponse response = gateway.test();
        assertEquals("OK", response.getService());
        assertEquals("OK", response.getTest());
    }
}
