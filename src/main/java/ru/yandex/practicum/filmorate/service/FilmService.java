package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(long filmId, long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(long filmId, long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id: " + userId + " не найден");
        }
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id: " + filmId + " не найден");
        }
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getFilmsMaxLike(int size) {
        if (size <= 0) {
            throw new IncorrectParameterException("size");
        }
        Collection<Film> filmsList = filmStorage.getFilms().values();
        return filmsList.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }
}