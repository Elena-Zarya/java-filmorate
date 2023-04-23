package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmServiceImpl(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage, @Qualifier("inMemoryUserStorage")
    UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(long filmId, long userId) {
        if (!userStorage.getAllUsers().contains(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        if (!filmStorage.getAllFilms().contains(filmId)) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getFilmsMaxLike(int size) {
        if (size <= 0) {
            throw new IncorrectParameterException("size");
        }
        Collection<Film> filmsList = filmStorage.getAllFilms();
        return filmsList.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Collection<Film> getFilms() {
        return null;
    }

    @Override
    public Film getFilm(long filmId) {
        return null;
    }
}