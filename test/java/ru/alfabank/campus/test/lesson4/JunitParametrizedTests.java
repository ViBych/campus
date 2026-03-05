package ru.alfabank.campus.test.lesson4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.alfabank.campus.payment.tests.lesson2.enums.PaymentStatuses;

import java.util.stream.Stream;

@Tag("smoke")
public class JunitParametrizedTests {

    @DisplayName("@ValueSource - параметризованный тест")
    @ParameterizedTest
    @ValueSource(strings = {"hello", "junit", "", "5"})
    void checkValueSourceStrings(String value) {
        Assertions.assertFalse(value.isEmpty(), "Строка должна не быть пустой");
    }

    @DisplayName("@ValueSource - параметризованный тест числа")
    @ParameterizedTest
    @ValueSource(ints = {3, 12, 99, 2})
    void checkValueSourceInts(int value) {
        Assertions.assertTrue(value > 0);
    }

    @DisplayName("@EnumSource - как источник данных")
    @ParameterizedTest(name = "Статус: {0}")
//    @EnumSource(PaymentStatuses.class)
    @EnumSource(value = PaymentStatuses.class,
            names = {"NEW", "ON_SIGN"},
            mode = EnumSource.Mode.EXCLUDE)
    void checkEnumValues(PaymentStatuses status) {
        System.out.println(status);
    }

    @DisplayName("@CsvSource - CSV как источник данных")
    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "1, 2, 3",
            "10, 10, 20",
            "100, 150, 250",
            "50, 49, 1"
    })
    void checkCSVSourceValuesInts(int a, int b, int expected) {
        Assertions.assertEquals(expected, a + b);
    }

    @DisplayName("@CsvSource - CSV как источник данных")
    @ParameterizedTest
    @CsvSource({
        "hello, hello",
        "junit, junit5",
        "'hello junit', 'hello junit5'"
    })
    void checkCSVSourceValuesString(String expected, String actual) {
        System.out.println(expected);
        Assertions.assertEquals(expected, actual);
//        Assertions.assertEquals(expected, a + b);
    }

    @ParameterizedTest
    @MethodSource("getValues")
    void checkMethodSourceValues(String input, int value) {
        System.out.println(input + " " + value);
//        System.out.println(value);
    }

    static Stream<Arguments> getValues() {
        return Stream.of(
                Arguments.of("Hello", 04),
                Arguments.of("Junit5", 2),
                Arguments.of("", 2112),
                Arguments.of("Тест")
        );
    }

}
