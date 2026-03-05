package ru.alfabank.campus.test.lesson4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.alfabank.campus.payment.tests.lesson2.entities.responses.account.AccountResponse;

import static org.junit.jupiter.api.Assertions.*;

public class JunitBaseTests {

    @Test
    @DisplayName("Первый тест-пример")
    void firstTest() {
        assertEquals(4, 4);
    }

    @Test
    @Disabled("Тест выключен потому что не работает. Необходима доработка теста")
    void secondTest() {
        assertEquals(4, 5);
    }

    @Test
    @DisplayName("assertEquals - проверка на равенство")
    void assertEqualsTest() {
        String expected = "Junit 5";
        String actual = "Junit " + 6;
        assertEquals(expected, actual, "Версии junit должны совпадать");
//        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("assertTrue / assertFalse - булевы проверки")
    void assertTrueFalseTest() {
        assertTrue(true);
        assertFalse(false);
        assertTrue(10 > 5);
        assertFalse("junit5".isEmpty(), "Строка не должна быть пустой");
    }

    @Test
    @DisplayName("assertNull / assertNotNull - проверка на null")
    void assertNullNotNullTest() {
        AccountResponse response = null;
        AccountResponse response1 = new AccountResponse();
        response1.setAccountNumberFull("4040404");

        assertNull(response);
        assertNotNull(response, "Полный номер счета должен существовать");
    }

    @Test
    @DisplayName("assertSame / aseertNotSame - проверка ссылочного значения")
    void assertSameNotSameTest() {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "hello";

        assertSame(s1, s2);
        assertSame(s1, s3);
    }

    @Test
    @DisplayName("assertAll - группировка проверок")
    void assertAllTest() {


//        assertTrue(name.startsWith("Sergei"), "Имя должно начинаться с Sergei");
//        assertTrue(name.endsWith("Kozelkov"), "Имя должно заканчиваться на Kozelkov");
//        assertTrue(name.contains(" "), "Имя должно содержать пробел");
//        assertEquals(15, name.length(), "Имя должно содержать 15 символов");

        String name = "S2ergei Kozelkov";

        assertAll("Проверка имени",
                () -> assertTrue(name.startsWith("Sergei"), "Имя должно начинаться с Sergei"),
                () -> assertTrue(name.endsWith("Kozelkov"), "Имя должно заканчиваться на Kozelkov"),
                () -> assertTrue(name.contains(" "), "Имя должно содержать пробел"),
                () -> assertEquals(15, name.length(), "Имя должно содержать 15 символов")
        );
    }

    @Test
    @DisplayName("assertThrows - проверка выброса исключения")
    void assertThrowsTest() {
        assertThrows(ArithmeticException.class,
                () -> {
                    int result = 10 / 0;
                },
                "Деление на 0 должно бросать экспепшен"
        );
    }

}
