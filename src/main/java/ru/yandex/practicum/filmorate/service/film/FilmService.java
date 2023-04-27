package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film addLike(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> getFilmsMaxLike(int size);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Film getFilm(long filmId);

}