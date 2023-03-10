package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    @Autowired
    private FilmStorage filmStorage;

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.getUsers().values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmStorage.addFilm(film);
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", film.getName());
        return film;
    }
}