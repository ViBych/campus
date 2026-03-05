package ru.alfabank.campus.test.lesson4;


import org.junit.jupiter.api.*;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JunitLifeRecycleTests {

//    private static int counter = 0;
    private int counter = 0;

    @BeforeAll
    static void setupAll() {
        System.out.println("[@BeforeAll - выполняется один раз перед тестами]");
    }

    @BeforeEach
    void setUp() {
        System.out.println("[@BeforeEach - выполняется перед каждым тестом]");
        counter++;
        System.out.println(counter);
    }

    @Test
    void firstTest() {
        System.out.println("Первый тест");
    }

    @Test
    void secondTest() {
        System.out.println("Второй тест");
    }

    @Test
    void thirthTest() {
        System.out.println("Третий тест");
    }

    @AfterEach
    void tearDown() {
        System.out.println("[@BeforeEach - выполняется после каждого теста]");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("[@BeforeAll - выполняется один раз после тестов]");
    }
}
