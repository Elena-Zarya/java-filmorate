package ru.yandex.practicum.filmorate.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = FutureCustomValidator.class)
public @interface FutureCustom {
    String date() default "";

    String message() default "Неправильная дата";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}