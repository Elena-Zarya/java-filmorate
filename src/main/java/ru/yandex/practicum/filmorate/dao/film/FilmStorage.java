package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    Collection<Film> getAllFilms();

    Film getFilm(long filmId);

    Film addLike(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> getFilmsMaxLike(int size);
}
