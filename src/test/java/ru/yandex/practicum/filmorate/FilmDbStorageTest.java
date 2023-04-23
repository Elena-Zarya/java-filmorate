package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    private Film film;

    @BeforeEach
    void createFilm() {
        Rating mpa = new Rating();
        mpa.setId(1);

        film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.parse("1967-03-25"));
        film.setDuration(100);
        film.setMpa(mpa);
    }

    @Test
    void addFilmTest() {
        filmDbStorage.addFilm(film);
        Film filmTest = filmDbStorage.getFilm(film.getId());
        assertThat(filmTest.getId()).isEqualTo(1);
        assertThat(filmTest.getName()).isEqualTo("nisi eiusmod");
        assertThat(filmTest.getDescription()).isEqualTo("adipisicing");
        assertThat(filmTest.getDuration()).isEqualTo(100);
        assertThat(filmTest.getReleaseDate()).isEqualTo("1967-03-25");
        assertThat(filmTest.getMpa().getId()).isEqualTo(1);
        filmDbStorage.deleteFilm(film.getId());
    }

    @Test
    void updateFilmTest() {
        filmDbStorage.addFilm(film);
        film.setDuration(120);
        Film filmTest = filmDbStorage.updateFilm(film);
        assertThat(filmTest.getDuration()).isEqualTo(120);
        filmDbStorage.deleteFilm(film.getId());
    }

    @Test
    void deleteFilmTest() {
        filmDbStorage.addFilm(film);
        filmDbStorage.deleteFilm(film.getId());
        Collection<Film> films = filmDbStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(0);
    }

    @Test
    void getAllFilmsTest() {
        filmDbStorage.addFilm(film);
        Collection<Film> films = filmDbStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(1);
        filmDbStorage.deleteFilm(film.getId());
    }

    @Test
    void getFilmTest() {
        filmDbStorage.addFilm(film);
        Film filmsTest = filmDbStorage.getFilm(film.getId());
        assertThat(filmsTest.getName()).isEqualTo(film.getName());
        filmDbStorage.deleteFilm(film.getId());
    }

    @Test
    void addLikeTest() {
        User user = new User();
        user.setLogin("dolore");
        user.setBirthday(LocalDate.parse("1946-08-20"));
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        userDbStorage.addUser(user);
        filmDbStorage.addFilm(film);
        filmDbStorage.addLike(film.getId(), user.getId());
        Film filmTest = filmDbStorage.getFilm(film.getId());
        assertThat(filmTest.getLikes().size()).isEqualTo(1);
        filmDbStorage.deleteFilm(film.getId());
        userDbStorage.deleteUser(user.getId());
    }

    @Test
    void deleteLikeTest() {
        User user = new User();
        user.setLogin("dolore");
        user.setBirthday(LocalDate.parse("1946-08-20"));
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        userDbStorage.addUser(user);
        filmDbStorage.addFilm(film);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.deleteLike(film.getId(), user.getId());
        Film filmTest = filmDbStorage.getFilm(film.getId());
        assertThat(filmTest.getLikes().size()).isEqualTo(0);
        filmDbStorage.deleteFilm(film.getId());
        userDbStorage.deleteUser(user.getId());
    }

    @Test
    void getFilmsMaxLikeTest() {
        Rating mpa = new Rating();
        mpa.setId(1);
        Film film2 = new Film();
        film2.setName("olav");
        film2.setDescription("adipisicing");
        film2.setReleaseDate(LocalDate.parse("1965-03-02"));
        film2.setDuration(150);
        film2.setMpa(mpa);
        User user = new User();
        user.setLogin("dolore");
        user.setBirthday(LocalDate.parse("1946-08-20"));
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        userDbStorage.addUser(user);
        filmDbStorage.addFilm(film);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addLike(film.getId(), user.getId());
        List<Film> topFilms = filmDbStorage.getFilmsMaxLike(1);
        Film filmTest = topFilms.get(0);
        assertThat(filmTest.getName()).isEqualTo(film.getName());
        filmDbStorage.deleteFilm(film.getId());
        filmDbStorage.deleteFilm(film2.getId());
        userDbStorage.deleteUser(user.getId());
    }
}
