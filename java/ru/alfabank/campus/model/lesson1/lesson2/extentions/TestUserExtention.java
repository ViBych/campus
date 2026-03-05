package ru.alfabank.campus.model.lesson1.lesson2.extentions;

import org.junit.jupiter.api.extension.*;
import ru.alfabank.campus.model.lesson1.lesson2.annotations.TestUser;
import ru.alfabank.campus.payment.tests.lesson2.enums.UserRights;
import ru.alfabank.campus.payment.tests.lesson2.utils.PaymentTestData;

import java.lang.reflect.Field;
import java.util.List;

public class TestUserExtention implements BeforeEachCallback, ParameterResolver {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            TestUser testUser = method.getAnnotation(TestUser.class);
            if (testUser != null) {
                List<UserRights> rights = List.of(testUser.withRights());
                PaymentTestData testData = PaymentTestData.generate(rights);
//                System.out.println(testData.getUserId() + " дошли до вызова");
            }
        });
    }

    private void injectTestUser(ExtensionContext context, PaymentTestData testData) {
        Object testInstance = context.getRequiredTestInstance();
        Class<?> currentClass = testInstance.getClass();

        while (currentClass != null) {
            try {
                Field field = currentClass.getDeclaredField("user");
                field.setAccessible(true);
                field.set(testInstance, testData);
                return;
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Произошла ошибка при инжекте в поле юзер");
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(PaymentTestData.class)
            && extensionContext.getTestMethod()
            .map(method -> method.getAnnotation(TestUser.class) != null)
            .orElse(false);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        TestUser testUser = extensionContext.getTestMethod()
            .map(method -> method.getAnnotation(TestUser.class))
            .orElseThrow(() -> new ParameterResolutionException("Аннотация @TestUser не найдена"));

        return PaymentTestData.generate(List.of(testUser.withRights()));
    }
}
