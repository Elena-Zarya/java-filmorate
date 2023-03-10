package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmTest {
    private static Validator validator;

    @BeforeAll
    private static void setValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateName() {
        Film film = new Film();
        film.setName(" ");
        film.setDuration(50);
        film.setReleaseDate(LocalDate.parse("2024-12-12"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Тест аннотации @NotBlank для поля name не пройден");
    }

    @Test
    void validateDuration() {
        Film film = new Film();
        film.setName("name");
        film.setDuration(0);
        film.setReleaseDate(LocalDate.parse("2024-12-12"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Тест аннотации @Positive для поля duration не пройден");
    }

    @Test
    void validateReleaseDate() {
        Film film = new Film();
        film.setName("name");
        film.setDuration(50);
        film.setReleaseDate(LocalDate.parse("1895-12-12"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Тест аннотации @FutureCustom для поля releaseDate не пройден");
    }

    @Test
    void validateDescription() {
        Film film = new Film();
        film.setDescription("Description Description  Description Description Description Description Description " +
                "Description Description Description Description Description Description Description  Description " +
                "Description  Description");
        film.setName("name");
        film.setDuration(50);
        film.setReleaseDate(LocalDate.parse("2024-12-12"));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Тест аннотации  @Length для поля description не пройден");
    }
}
