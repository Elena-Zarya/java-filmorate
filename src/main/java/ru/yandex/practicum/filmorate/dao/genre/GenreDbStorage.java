package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQueryGenre = "SELECT* FROM genre";
        return jdbcTemplate.query(sqlQueryGenre, this::mapRowToGenre);
    }

    @Override
    public Genre getGenre(long genreId) {
        try {
            String sqlQuery = "SELECT* FROM genre WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
        } catch (Exception e) {
            throw new NotFoundException("Жанр с id: " + genreId + " не найден");
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }
}



