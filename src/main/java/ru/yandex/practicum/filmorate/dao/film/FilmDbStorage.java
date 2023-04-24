package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "SELECT film_id FROM films WHERE film_name = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, film.getName());
        if (!filmRows.next()) {
            jdbcTemplate.update("INSERT INTO films (film_name, description, release_date, duration, rating_id) " +
                            "VALUES (?, ?, ?, ?, ?)", film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId());
            SqlRowSet filmRows2 = jdbcTemplate.queryForRowSet(sqlQuery, film.getName());
            if (filmRows2.next()) {
                film.setId(filmRows2.getLong("film_id"));
                List<Genre> genres = film.getGenres();
                if (!genres.isEmpty()) {
                    for (Genre genre : genres) {
                        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                                film.getId(), genre.getId());
                    }
                }

            } else {
                log.info("Для фильма " + film.getName() + " не найден ID");
            }
            log.info("Добавлен фильм: {}", film.getName());
        } else {
            log.info("Фильм " + film.getName() + " уже добавлен");
            throw new NotFoundException("Фильм " + film.getName() + " уже добавлен");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        long filmId = film.getId();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT* FROM films WHERE film_id = ?", filmId);
        if (filmRows.next()) {
            jdbcTemplate.update("UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?," +
                            " rating_id = ? WHERE film_id = ? ",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getMpa().getId(), filmId);

            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
            for (Genre genre : film.getGenres()) {
                SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet("SELECT* FROM film_genre " +
                        "WHERE film_id = ? AND genre_id = ?", filmId, genre.getId());
                if (!filmGenreRows.next()) {
                    jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                            filmId, genre.getId());
                }
            }
            log.info("Обновлены данные фильма: {} {}", filmId, film.getName());
        } else {
            log.info("Фильм с ID {} не найден.", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        return getFilm(filmId);
    }

    @Override
    public void deleteFilm(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT* FROM films WHERE film_id = ?", id);
        if (filmRows.next()) {
            SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet("SELECT* FROM film_genre WHERE film_id = ?", id);
            if (filmGenreRows.next()) {
                jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", id);
            }
            SqlRowSet filmLikesRows = jdbcTemplate.queryForRowSet("SELECT* FROM film_likes WHERE film_id = ?", id);
            if (filmLikesRows.next()) {
                jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ?", id);
            }
            jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
            log.info("Удален фильм с id: {} ", id);
        } else {
            log.info("Фильм с ID {} не найден.", id);
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> films;
        String sqlQueryFilms = "SELECT film_id, film_name, description, duration, release_date, rating_id, FROM films ";
        String sqlQueryRating = "SELECT r.rating_id, r.rating_name FROM rating r " +
                "JOIN films f ON f.rating_id = r.rating_id WHERE f.film_id = ?";
        String sqlQueryGenre = "SELECT g.genre_id, g.genre_name FROM genre g " +
                "JOIN film_genre fg ON g.genre_id = fg.genre_id " +
                "JOIN films f ON fg.film_id = f.film_id " +
                "WHERE f.film_id = ?";
        films = jdbcTemplate.query(sqlQueryFilms, this::mapRowToFilms);
        for (Film film : films) {
            List<Genre> genres = jdbcTemplate.query(sqlQueryGenre, this::mapRowToGenre, film.getId());
            Rating mpa = jdbcTemplate.queryForObject(sqlQueryRating, this::mapRowToRating, film.getId());
            film.setGenres(genres);
            film.setMpa(mpa);
        }
        return films;
    }

    @Override
    public Film getFilm(long filmId) {
        try {
            Film film;
            String sqlQueryFilms = "SELECT* FROM films WHERE film_id = ?";
            String sqlQueryRating = "SELECT r.rating_id, r.rating_name FROM rating r " +
                    "JOIN films f ON f.rating_id = r.rating_id WHERE f.film_id = ?";
            String sqlQueryGenre = "SELECT g.genre_id, g.genre_name FROM genre g " +
                    "JOIN film_genre fg ON g.genre_id = fg.genre_id " +
                    "JOIN films f ON fg.film_id = f.film_id " +
                    "WHERE f.film_id = ?";
            String sqlQueryLikes = "SELECT user_id FROM film_likes WHERE film_id = ?";
            film = jdbcTemplate.queryForObject(sqlQueryFilms, this::mapRowToFilms, filmId);
            List<Genre> genres = jdbcTemplate.query(sqlQueryGenre, this::mapRowToGenre, filmId);
            Rating mpa = jdbcTemplate.queryForObject(sqlQueryRating, this::mapRowToRating, filmId);
            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQueryLikes, filmId);
            if (filmRows.next()) {
                film.getLikes().add(filmRows.getLong("user_id"));
            }
            film.setGenres(genres);
            film.setMpa(mpa);
            return film;
        } catch (Exception e) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }

    @Override
    public Film addLike(long filmId, long userId) {
        String sqlQuery = "SELECT* FROM films WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (filmRows.next()) {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", userId);
            if (userRows.next()) {
                jdbcTemplate.update("MERGE INTO film_likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
            } else {
                throw new NotFoundException("Пользователь с id: " + userId + " не найден");
            }
        } else {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        return getFilm(filmId);
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        String sqlQuery = "SELECT* FROM films WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (filmRows.next()) {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", userId);
            if (userRows.next()) {
                SqlRowSet filmLikeRows = jdbcTemplate.queryForRowSet("SELECT* FROM film_likes WHERE film_id = ? " +
                        "AND user_id = ?", filmId, userId);
                if (filmLikeRows.next()) {
                    jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?", filmId, userId);
                } else {
                    throw new NotFoundException("У фильма с id: " + filmId + " не найден лайк от пользователя " +
                            "с id " + userId);
                }
            } else {
                throw new NotFoundException("Пользователь с id: " + userId + " не найден");
            }
        } else {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        return getFilm(filmId);
    }

    @Override
    public List<Film> getFilmsMaxLike(int count) {
        List<Film> topFilms;
        SqlRowSet filmLikeRows = jdbcTemplate.queryForRowSet("SELECT* FROM films");
        if (filmLikeRows.next()) {
            String sqlQuery = "SELECT f.film_id , f.film_name, f.description, f.release_date, " +
                    "f.duration integer, COUNT(fl.user_id) AS count_likes FROM films f " +
                    "LEFT JOIN film_likes fl ON f.film_id = fl.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY count_likes DESC " +
                    "LIMIT ?";
            topFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilms, count);
            for (Film film : topFilms) {
                String sqlQueryRating = "SELECT r.rating_id, r.rating_name FROM rating r " +
                        "JOIN films f ON f.rating_id = r.rating_id WHERE f.film_id = ?";
                String sqlQueryGenre = "SELECT g.genre_id, g.genre_name FROM genre g " +
                        "JOIN film_genre fg ON g.genre_id = fg.genre_id " +
                        "JOIN films f ON fg.film_id = f.film_id " +
                        "WHERE f.film_id = ?";
                List<Genre> genres = jdbcTemplate.query(sqlQueryGenre, this::mapRowToGenre, film.getId());
                Rating mpa = jdbcTemplate.queryForObject(sqlQueryRating, this::mapRowToRating, film.getId());
                film.setGenres(genres);
                film.setMpa(mpa);
            }
        } else {
            throw new NotFoundException("Фильмы отсутствуют");
        }
        return topFilms;
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        return film;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        Rating mpa = new Rating();
        mpa.setId(resultSet.getInt("rating_id"));
        mpa.setName(resultSet.getString("rating_name"));
        return mpa;
    }
}
