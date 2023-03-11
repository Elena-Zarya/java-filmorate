package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateEmail() {
        User user = new User();
        user.setEmail("email");
        user.setLogin("login");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Поле email не прошло валидацию");
    }

    @Test
    void validateEmailNotBlank() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("login");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Поле email не прошло валидацию");
    }

    @Test
    void validateBirthday() {
        User user = new User();
        user.setEmail("email@m");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("2024-12-12"));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Поле email не прошло валидацию");
    }

    @Test
    void validateLogin() {
        User user = new User();
        user.setEmail("email@m");
        user.setLogin(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Поле email не прошло валидацию");
    }
}
