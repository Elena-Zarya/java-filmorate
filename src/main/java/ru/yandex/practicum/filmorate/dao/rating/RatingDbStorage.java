package ru.yandex.practicum.filmorate.dao.rating;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Rating> getAllRatings() {
        String sqlQueryGenre = "SELECT* FROM rating";
        return jdbcTemplate.query(sqlQueryGenre, this::mapRowToRating);
    }

    @Override
    public Rating getRating(long ratingId) {
        try {
            String sqlQuery = "SELECT* FROM rating WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, ratingId);
        } catch (Exception e) {
            throw new NotFoundException("Рейтинг с id: " + ratingId + " не найден");
        }
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        Rating mpa = new Rating();
        mpa.setId(resultSet.getInt("rating_id"));
        mpa.setName(resultSet.getString("rating_name"));
        return mpa;
    }
}
