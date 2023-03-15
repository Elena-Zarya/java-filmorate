package ru.yandex.practicum.filmorate.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureCustomValidator implements ConstraintValidator<FutureCustom, LocalDate> {
    private String date;

    @Override
    public void initialize(FutureCustom constraintAnnotation) {
        this.date = constraintAnnotation.date();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate d = LocalDate.parse(date);
        return value.isAfter(d);
    }
}