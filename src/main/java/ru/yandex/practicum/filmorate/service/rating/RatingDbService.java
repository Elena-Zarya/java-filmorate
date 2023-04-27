package ru.yandex.practicum.filmorate.service.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.rating.RatingStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingDbService implements RatingService {
    private final RatingStorage ratingStorage;

    @Override
    public Collection<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    @Override
    public Rating getRating(long ratingId) {
        return ratingStorage.getRating(ratingId);
    }
}
