package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.service.FutureCustom;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @FutureCustom(date = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}