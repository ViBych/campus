package ru.alfabank.campus.test.lesson5.parallel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.alfabank.campus.payment.tests.lesson2.extensions.TimeExtension;

@ExtendWith(TimeExtension.class)
@DisplayName("Тестовый класс 1")
public class TestClass1 {

    @Test
    @Tag("smoke")
    @DisplayName("Тест №1 класса [1]")
    void test1() throws InterruptedException {
        System.out.println("Тест №1 класса [1]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }

    @Test
    @Tag("payment")
    @DisplayName("Тест №2 класса [1]")
    void test2() throws InterruptedException {
        System.out.println("Тест №2 класса [1]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }

    @Test
    @Tag("payment")
    @DisplayName("Тест №3 класса [1]")
    void test3() throws InterruptedException {
        System.out.println("Тест №3 класса [1]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }

    @Test
    @DisplayName("Тест №4 класса [1]")
    void test4() throws InterruptedException {
        System.out.println("Тест №4 класса [1]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }
}
