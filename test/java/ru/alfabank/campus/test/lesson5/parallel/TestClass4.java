package ru.alfabank.campus.test.lesson5.parallel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.alfabank.campus.payment.tests.lesson2.extensions.TimeExtension;

@ExtendWith(TimeExtension.class)
@DisplayName("Тестовый класс 4")
public class TestClass4 {
    
    @Test
    @DisplayName("Тест №1 класса [4]")
    void test1() throws InterruptedException {
        System.out.println("Тест №1 класса [4]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }
    
    @Test
    @DisplayName("Тест №2 класса [4]")
    void test2() throws InterruptedException {
        System.out.println("Тест №2 класса [4]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }
    
    @Test
    @DisplayName("Тест №3 класса [4]")
    void test3() throws InterruptedException {
        System.out.println("Тест №3 класса [4]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }
    
    @Test
    @DisplayName("Тест №4 класса [4]")
    void test4() throws InterruptedException {
        System.out.println("Тест №4 класса [4]");
        Thread.sleep(2500);
        Assertions.assertEquals(1, 1);
    }
}
