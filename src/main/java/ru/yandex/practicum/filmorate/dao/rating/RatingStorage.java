package ru.yandex.practicum.filmorate.dao.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

public interface RatingStorage {
    Collection<Rating> getAllRatings();

    Rating getRating(long ratingId);
}
