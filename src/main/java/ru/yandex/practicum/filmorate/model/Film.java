package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.service.validator.FutureCustom;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JsonIgnore
    private final Set<Long> likes = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();
    private Rating mpa;
}