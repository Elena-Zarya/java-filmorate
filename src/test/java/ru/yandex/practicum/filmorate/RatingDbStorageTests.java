package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTests {

    private final RatingDbStorage ratingDbStorage;

    @Test
    void getAllRatingsTest() {
        final Collection<Rating> ratings = ratingDbStorage.getAllRatings();
        assertThat(ratings).size().isEqualTo(5);
    }

    @Test
    void getRatingTest() {
        final Rating rating = ratingDbStorage.getRating(2);

        assertThat(rating.getId()).isEqualTo(2);
        assertThat(rating.getName()).isEqualTo("PG");
    }
}

