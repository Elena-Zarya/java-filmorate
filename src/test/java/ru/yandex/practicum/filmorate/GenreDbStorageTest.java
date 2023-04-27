package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void getAllGenresTest() {
        final Collection<Genre> ratings = genreDbStorage.getAllGenres();
        assertThat(ratings).size().isEqualTo(6);
    }

    @Test
    void getGenreTest() {
        final Genre genre = genreDbStorage.getGenre(2);

        assertThat(genre.getId()).isEqualTo(2);
        assertThat(genre.getName()).isEqualTo("Драма");
    }
}
