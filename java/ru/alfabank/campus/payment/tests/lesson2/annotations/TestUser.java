package ru.alfabank.campus.payment.tests.lesson2.annotations;

import ru.alfabank.campus.payment.tests.lesson2.enums.UserRights;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestUser {
    UserRights[] withRights() default {};
}
