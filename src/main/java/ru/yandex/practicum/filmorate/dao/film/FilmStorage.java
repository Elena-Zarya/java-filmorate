package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(long id);

    Map<Long, Film> getFilms();

    Film getFilm(long filmId);
}
