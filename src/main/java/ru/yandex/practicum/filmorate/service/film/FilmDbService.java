package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Service
public class FilmDbService implements FilmService {

    FilmStorage filmStorage;

    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addLike(long filmId, long userId) {
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getFilmsMaxLike(int count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmStorage.getFilmsMaxLike(count);
    }

    @Override
    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return filmStorage.getFilm(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {

        return filmStorage.updateFilm(film);
    }

    @Override
    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(long filmId) {
        return filmStorage.getFilm(filmId);
    }
}
