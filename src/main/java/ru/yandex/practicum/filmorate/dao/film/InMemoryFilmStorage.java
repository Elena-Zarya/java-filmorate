package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    @Override
    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(id, film);
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
        } else {
            throw new FilmNotFoundException("Фильм с id: " + id + "не найден");
        }
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new FilmNotFoundException("Фильм с id: " + id + "не найден");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return getFilms().values();
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    private long generateId() {
        return ++id;
    }

    public Film getFilm(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id: " + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public Film addLike(long filmId, long userId) {
        return null;
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        return null;
    }

    @Override
    public List<Film> getFilmsMaxLike(int size) {
        return null;
    }
}
