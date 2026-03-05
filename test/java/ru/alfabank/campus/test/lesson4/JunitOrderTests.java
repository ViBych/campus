package ru.alfabank.campus.test.lesson4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JunitOrderTests {

    @Test
    @Order(111)
    void firstTest() {
        System.out.println("Первый тест");
    }

    @Test
    @Order(2)
    void secondTest() {
        System.out.println("Второй тест");
    }

    @Test
    @Order(3)
    void thirdTest() {
        System.out.println("Третий тест");
    }

    @RepeatedTest(value = 3, name = "Повтор")
    @DisplayName("Тест запускается несколько раз")
    void repeatedTest(RepetitionInfo repetitionInfo) {
        System.out.println("Запуск " + repetitionInfo.getCurrentRepetition());
    }
}
