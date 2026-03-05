package ru.alfabank.campus.test.lesson4;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
public class JunitTagsTests {

    @Tag("myTest")
    @Test
    void firstTest() {
        System.out.println("Первый тест");
    }

    @Tag("notMyTest")
    @Test
    void secondTest() {
        System.out.println("Второй тест");
    }

    @Tag("myTest")
    @Test
    void thirdTest() {
        System.out.println("Третий тест");
    }
}
