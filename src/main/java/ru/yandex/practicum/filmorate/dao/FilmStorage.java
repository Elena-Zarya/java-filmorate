package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    private long generateId() {
        return ++id;
    }

    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(id, film);
    }

    public void updateFilm(Film film) {
        long id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
        } else {
            throw new RuntimeException("Фильм с id: " + id + "не найден");
        }
    }

    public Map<Long, Film> getUsers() {
        return films;
    }
}
